package seedu.address.model.person;

import java.util.Locale;
import java.util.function.Predicate;

/**
 * Returns true if the Person has ANY of the target tags (case-insensitive).
 */
public class PersonHasAnyTagPredicate implements Predicate<Person> {

    private final String targetTagLower; // lowercased for case-insensitive compare

    /**
     * Checks if the Person has the target tag (case-insensitive).
     */
    public PersonHasAnyTagPredicate(String targetTags) {
        this.targetTagLower = targetTags.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean test(Person person) {
        if (targetTagLower.isEmpty() || targetTagLower.isBlank()) {
            return false; // fail-closed
        }

        // check if the person is a senior
        if (!(person instanceof Senior)) {
            return false;
        }
        Tag personTags = ((Senior) person).getRiskTag();
        // Build a lowercased set of the person's tag names
        String personTagLower = personTags.getTagName()
                .toLowerCase(Locale.ROOT); // if Tag has getter, use t.getTagName()

        return targetTagLower.equals(personTagLower);
    }

    @Override
    public String toString() {
        return "PersonHasAnyTagPredicate" + targetTagLower;
    }
}
