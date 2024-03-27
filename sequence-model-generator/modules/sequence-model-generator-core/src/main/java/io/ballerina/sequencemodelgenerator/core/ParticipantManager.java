package io.ballerina.sequencemodelgenerator.core;

import io.ballerina.compiler.api.SemanticModel;
import io.ballerina.compiler.api.symbols.Symbol;
import io.ballerina.compiler.syntax.tree.NameReferenceNode;
import io.ballerina.compiler.syntax.tree.Node;
import io.ballerina.compiler.syntax.tree.NonTerminalNode;
import io.ballerina.compiler.syntax.tree.SyntaxTree;
import io.ballerina.projects.Project;
import io.ballerina.sequencemodelgenerator.core.model.Participant;
import io.ballerina.tools.diagnostics.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantManager {

    private static ParticipantManager instance = null;
    private final Map<String, String> cache;
    private final List<Participant> participants;
    private final SemanticModel semanticModel;
    private final Project project;

    private ParticipantManager(SemanticModel semanticModel, Project project) {
        this.cache = new HashMap<>();
        this.participants = new ArrayList<>();
        this.semanticModel = semanticModel;
        this.project = project;
    }

    public static void initialize(SemanticModel semanticModel, Project project) {
        instance = new ParticipantManager(semanticModel, project);
    }

    public static ParticipantManager getInstance() {
        return instance;
    }

    public String getParticipantId(NameReferenceNode name, String sourceId) {
        String participantId = cache.get(name.toString());
        if (participantId != null) {
            return participantId;
        }
        try {
            Symbol symbol = semanticModel.symbol(name).orElseThrow();
            Location location = symbol.getLocation().orElseThrow();
            String fileName = location.lineRange().fileName();
            String moduleName = CommonUtil.getModuleName(symbol).orElseThrow();
            SyntaxTree syntaxTree = CommonUtil.getSyntaxTree(project, fileName, moduleName);
            NonTerminalNode participantNode = CommonUtil.getNode(syntaxTree, location.textRange());
            return generateParticipant(participantNode, sourceId, moduleName);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public String generateParticipant(Node participantNode, String sourceId, String moduleName) {
        ParticipantAnalyzer participantAnalyzer = new ParticipantAnalyzer(semanticModel, sourceId, moduleName);
        participantNode.accept(participantAnalyzer);
        Participant participant = participantAnalyzer.getParticipant();
        participants.add(participant);
        String participantId = participant.id();
        cache.put(participant.name(), participantId);
        return participantId;
    }

    public List<Participant> getParticipants() {
        return participants;
    }
}
