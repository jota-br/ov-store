package ostro.veda.service.events;

import java.lang.reflect.InvocationTargetException;

public interface EventService {

    void handleEvent(EventPayload payload)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException;
}
