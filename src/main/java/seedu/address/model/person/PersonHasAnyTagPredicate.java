package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.model.tag.Tag;

/**
 * Returns true if the Person has ANY of the target tags (case-insensitive).
 */
public class PersonHasAnyTagPredicate implements Predicate<Person> {

    private final List<String> targetTagsLower; // lowercased for case-insensitive compare

    /**
     * Checks if the Person has ANY of the target tags (case-insensitive).
     */
    public PersonHasAnyTagPredicate(List<String> targetTags) {
        this.targetTagsLower = targetTags.stream()
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());
    }

    @Override
    public boolean test(Person person) {
        // check if the person is a senior
        if (!(person instanceof Senior)) {
            return false;
        }
        Set<Tag> personTags = ((Senior) person).getRiskTags();
        // Build a lowercased set of the person's tag names
        Set<String> personTagNamesLower = personTags.stream()
                .map(t -> t.tagName.toLowerCase(Locale.ROOT)) // if Tag has getter, use t.getTagName()
                .collect(Collectors.toSet());

        for (String q : targetTagsLower) {
            if (personTagNamesLower.contains(q)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PersonHasAnyTagPredicate
                && Objects.equals(targetTagsLower, ((PersonHasAnyTagPredicate) other).targetTagsLower));
    }

    @Override
    public String toString() {
        return "PersonHasAnyTagPredicate" + targetTagsLower;
    }
}
