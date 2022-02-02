package img.imaginary;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import img.imaginary.config.UniversityStarterConfig;

public class UniversityStarter {

    public static void main(String[] args) {
        ApplicationContext configContext = new AnnotationConfigApplicationContext(UniversityStarterConfig.class);
    }
}
