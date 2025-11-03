package seedu.address.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.model.Model;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the senior in the {@code model}'s senior list at {@code index}.
     */
    public static Senior getSenior(Model model, Integer index) {
        return model.getFilteredSeniorList().stream()
                .filter(s -> {
                    Integer seniorId = s.getId();
                    return seniorId != null && (seniorId.equals(index));
                })
                .findFirst()
                .orElse(null);
    }

    /**
    * Returns the caregiver in the {@code model}'s caregiver list at {@code index}.
    */
    public static Caregiver getCaregiver(Model model, Integer index) {
        return model.getFilteredCaregiverList().stream()
             .filter(c -> {
                 Integer caregiverId = c.getId();
                 return caregiverId != null && (caregiverId.equals(index));
             })
             .findFirst()
             .orElse(null);
    }
}
