package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;
import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        for (Method method : Address.class.getDeclaredMethods()) {

            // Проверяем, есть ли у метода аннотация @Inspect
            if (method.isAnnotationPresent(Inspect.class)) {

                var mOutType = method.getReturnType().getSimpleName();

                System.out.println("Method getCity" + method.getName() + " returns a value of type " + mOutType);
            }
        }
        // END
    }
}
