package com.camunda.bpmn.utils;

import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

import java.util.Collection;
import java.util.List;

public class TraverseTool {
    public static boolean traverseSequenceFlow(
            FlowNode flowStart,
            FlowNode flowEnd,
            List<FlowNode> path,
            List<String> visited,
            Collection<EndEvent> endEvents) {
        // it's necessary to know the current path and the already visited nodes to prevent falling into loops
        visited.add(flowStart.getId());
        path.add(flowStart);

        // this is the case where the flow start element has already completed the path
        if (flowStart.getId().equals(flowEnd.getId())) {
            return true;
        }

        // this case will prevent that the code fall into an infinite loop
        // if flowStart has already reached an endEvent, and it's not the final destination, it means that no path was found
        if (!endEvents.stream().filter((event) -> event.getId().equals(flowStart.getId())).toList().isEmpty()) {
            return false;
        }

        // this loop will check each one of the neighbor nodes
        // if the visited list already contains the id then that path shall be ignored
        for (SequenceFlow flow: flowStart.getOutgoing()) {
            if (!visited.contains(flow.getTarget().getId())) {
                if (traverseSequenceFlow(flow.getTarget(), flowEnd, path, visited, endEvents)){
                    return true;
                }
            }
        }

        // backtrack -> in case that no path is found in the previous loop then it will remove each one of the elements
        // added to the path, since that path goes nowhere
        path.removeLast();
        return false;
    }
}

