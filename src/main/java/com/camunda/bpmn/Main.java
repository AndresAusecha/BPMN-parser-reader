package com.camunda.bpmn;

import com.camunda.bpmn.clients.XmlClient;
import com.camunda.bpmn.parsers.XmlParser;
import com.camunda.bpmn.utils.TraverseTool;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String result = XmlClient.fetchXml();

            BpmnModelInstance model = XmlParser.readFromText(result);

            Collection<SequenceFlow> flows = model.getModelElementsByType(SequenceFlow.class);

            Collection<UserTask> userTasks = model.getModelElementsByType(UserTask.class);

            Collection<EndEvent> endEvents = model.getModelElementsByType(EndEvent.class);

            String startRef = args[0];
            String endRef = args[1];

            FlowNode startRefNode = null;
            FlowNode endRefNode = null;

            List<UserTask> startNodeSearchInTasks = userTasks.stream().filter((userTask) -> userTask.getId().equals(startRef)).toList();

            if (!startNodeSearchInTasks.isEmpty()) {
                startRefNode = startNodeSearchInTasks.getFirst();
            }

            if (startRefNode == null) {
                startRefNode = (FlowNode) flows
                        .stream()
                        .filter((flow) -> flow.getSource().getId().equals(startRef))
                        .toList()
                        .getFirst();
            }

            List<UserTask> endRefSearchUserTasks = userTasks.stream().filter((userTask) -> userTask.getId().equals(endRef)).toList();

            if (!endRefSearchUserTasks.isEmpty()) {
                endRefNode = endRefSearchUserTasks.getFirst();
            }

            List<EndEvent> endEventSearch = endEvents.stream().filter((userTask) -> userTask.getId().equals(endRef)).toList();

            if (!endEvents.isEmpty()) {
                endRefNode = endEventSearch.getFirst();
            }

            if (endRefNode == null) {
                endRefNode = (FlowNode) flows
                        .stream()
                        .filter((flow) -> flow.getSource().getId().equals(endRef))
                        .toList()
                        .getFirst();
            }

            List<FlowNode> path = new ArrayList<>();
            TraverseTool.traverseSequenceFlow(startRefNode, endRefNode, path, new ArrayList<>(), endEvents);

            if (path.isEmpty()) {
                throw new Exception("there is no path between the nodes");
            }

            StringBuilder finalMessageBuilder = new StringBuilder().append("The path from ");
            finalMessageBuilder.append(args[0]);
            finalMessageBuilder.append(" to ");
            finalMessageBuilder.append(args[1]);
            finalMessageBuilder.append(" is: [");

            short counter = 0;
            for (FlowNode node : path) {
                finalMessageBuilder.append(node.getId());

                if (counter < path.size() - 1) {
                    finalMessageBuilder.append(", ");
                }

                counter++;
            }

            finalMessageBuilder.append("]");

            System.out.println(finalMessageBuilder);
        } catch (Exception e) {
            System.err.println("an error occurred during the program execution and will exist with code -1");
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}