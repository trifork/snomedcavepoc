package dk.sst.snomedcave.dbgenerate;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Initiator {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:dbgenerateContext.xml");
        SnomedParser snomedParser = context.getBean(SnomedParser.class);

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name = " + name);
        }

        if (snomedParser == null) {
            throw new RuntimeException("Could not start");
        }

        if (snomedParser.conceptRepository == null) {
            throw new RuntimeException("Needs repo");
        }

        if (!ArrayUtils.contains(args, "--skip-concepts")) {
            System.out.println("Will import concepts");
            snomedParser.importConcept();
        }

        if (!ArrayUtils.contains(args, "--skip-relationships")) {
            System.out.println("Will import relationships");
            snomedParser.importRelationships();
        }

        context.destroy();
    }
}
