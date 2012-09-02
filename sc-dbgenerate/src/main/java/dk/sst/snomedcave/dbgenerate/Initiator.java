package dk.sst.snomedcave.dbgenerate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;

public class Initiator {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }

        File db = new File("target/data-insert.db");
        if (db.exists()) {
            FileUtils.deleteDirectory(db);
        }

        SnomedParser snomedParser = new SnomedParser();

        if (!ArrayUtils.contains(args, "--skip-concepts")) {
            System.out.println("Will import concepts");
            snomedParser.importConcept();
        }

        if (!ArrayUtils.contains(args, "--skip-relationships")) {
            System.out.println("Will import relationships");
            snomedParser.importRelationships();
        }

        if (!ArrayUtils.contains(args, "--skip-drugs")) {
            System.out.println("Will import drugs");
            snomedParser.importSubtances();
        }

        snomedParser.finish();
    }
}
