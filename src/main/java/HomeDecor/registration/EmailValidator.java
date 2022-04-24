package HomeDecor.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        if (s == null || s.isEmpty()){
            return false;
        }
        String RegExp = "[\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";
        Pattern pattern = Pattern.compile(RegExp);
        if (pattern.matcher(s).matches()) {
            return true;
        }
        else return false;
    }
}
