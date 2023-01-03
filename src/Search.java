import engine.core.MarioEvent;
import engine.helper.EventType;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Search {
    ArrayList<MarioEvent> gameEvents;

    Search(ArrayList<MarioEvent> gameEvents) {
        this.gameEvents = gameEvents;
    }

    public ArrayList<ArrayList<MarioEvent>> searchEvent(DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount();
        ArrayList<EventType> eventTypes = new ArrayList<>();
        ArrayList<EventParam> eventParams = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            eventTypes.add((EventType) tableModel.getValueAt(i, 0));
            eventParams.add((EventParam) tableModel.getValueAt(i, 1));
        }
        return searchEvent(eventTypes, eventParams);
    }

    public ArrayList<ArrayList<MarioEvent>> searchEvent(ArrayList<EventType> eventTypes, ArrayList<EventParam> eventParams) {
    int searchEventSize = gameEvents.size();
    ArrayList<ArrayList<MarioEvent>> ret = new ArrayList<>();
    if (searchEventSize == 0) {
        return ret;
    }
    for (int p = 0; p < gameEvents.size(); p++) {
        ArrayList<MarioEvent> result = searchEvent(eventTypes, eventParams, 0, p);
        if (!result.isEmpty()) {
            int start = result.get(0).getTime();
            int end = result.get(result.size() - 1).getTime();
            if (end - start <= 5 * 24) {
                // check if the last element is unique
                if (!ret.isEmpty()) {
                    ArrayList<MarioEvent> previousResult = ret.get(ret.size() - 1);
                    if (end == previousResult.get(previousResult.size() - 1).getTime()) {
                        ret.remove(ret.size() - 1);
                    }
                }
                ret.add(result);
            }
        }
    }
    return ret;
}

    private ArrayList<MarioEvent> searchEvent(ArrayList<EventType> eventTypes, ArrayList<EventParam> eventParams, int i, int pos) {
        int searchEventSize = eventTypes.size();
        ArrayList<MarioEvent> ret = new ArrayList<>();
        EventType targetEventType = eventTypes.get(i);
        EventParam targetEventParam = eventParams.get(i);
        MarioEvent event = gameEvents.get(pos);
        if (event.getEventType() == targetEventType.getValue() && event.getEventParam() == targetEventParam.getValue()) {
            if (i == searchEventSize - 1) {
                ret.add(event);
            } else {
                for (int p = pos + 1; p < gameEvents.size(); p++) {
                    ArrayList<MarioEvent> result = searchEvent(eventTypes, eventParams, i + 1, p);
                    if (!result.isEmpty()) {
                        ret.add(event);
                        ret.addAll(result);
                        return ret;
                    }
                }
            }
        }
        return ret;
    }
}
