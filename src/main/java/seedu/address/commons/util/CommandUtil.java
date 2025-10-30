package seedu.address.commons.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

/**
 * Utilities for common command validations and lookups.
 */
public class CommandUtil {
    /**
     * Validates that the given index is non-null and non-negative.
     * @throws CommandException if invalid.
     */
    public static void validateIndex(Integer index, String errorIfInvalid) throws CommandException {
        if (index == null || index < 0) {
            throw new CommandException(errorIfInvalid);
        }
    }

    /**
     * Returns the first item whose extracted id equals the given id, or null if none.
     * If {@code id} is null, this returns null without searching.
     */
    public static <T> T findFirstByIdOrNull(
            List<T> list,
            Integer id,
            Function<T, Integer> idGetter
    ) {
        Objects.requireNonNull(list, "list");
        Objects.requireNonNull(idGetter, "idGetter");

        if (id == null) {
            return null;
        }

        return list.stream()
                .filter(item -> {
                    Integer itemId = idGetter.apply(item);
                    return itemId != null && itemId.equals(id);
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the {@link Senior} whose {@code seniorId} matches the given id, or {@code null} if none is found.
     *
     * @param seniors list to search; must not be {@code null}
     * @param seniorId id to match; may be {@code null}
     * @return the matching {@link Senior}, or {@code null} if not found or if {@code seniorId} is {@code null}
     */
    public static Senior findSeniorByIdOrNull(
            List<Senior> seniors,
            Integer seniorId
    ) {
        return findFirstByIdOrNull(seniors, seniorId, Senior::getSeniorId);
    }

    /**
     * Returns the {@link Caregiver} whose {@code caregiverId} matches the given id, or {@code null} if none is found.
     *
     * @param caregiver list to search; must not be {@code null}
     * @param caregiverId id to match; may be {@code null}
     * @return the matching {@link Caregiver}, or {@code null} if not found or if {@code caregiverId} is {@code null}
     */
    public static Caregiver findCaregiverByIdOrNull(
            List<Caregiver> caregiver,
            Integer caregiverId
    ) {
        return findFirstByIdOrNull(caregiver, caregiverId, Caregiver::getCaregiverId);
    }

    /**
     * Returns the {@link Senior} whose {@code seniorId} matches the given id, or throws if none is found.
     *
     * @param seniors list to search and must not be null
     * @param seniorId id to match and must not be null
     * @param errorIfInvalid error message for the thrown exception if no match is found; must not be {@code null}
     * @return the matching {@link Senior}
     * @throws CommandException if {@code seniorId} is not found in {@code seniors}
     */
    public static Senior findSeniorById(
            List<Senior> seniors,
            Integer seniorId,
            String errorIfInvalid
    ) throws CommandException {
        Senior s = findSeniorByIdOrNull(seniors, seniorId);
        if (s == null) {
            throw new CommandException(errorIfInvalid);
        }
        return s;
    }

    /**
     * Validates an optional {@code seniorId}. If {@code seniorId} is {@code null}, returns {@code null}.
     * If non-null, returns the matching {@link Senior} or throws if no match is found.
     *
     * @param seniors        list to search; must not be {@code null}
     * @param seniorId       optional id to validate; may be {@code null}
     * @param errorIfInvalid error message for the thrown exception if {@code seniorId} is non-null but not found;
     *                       must not be {@code null}
     * @return the matching {@link Senior} when {@code seniorId} is non-null; otherwise {@code null}
     * @throws CommandException if {@code seniorId} is non-null and not found in {@code seniors}
     */
    public static Senior validateOptionalSeniorId(
            List<Senior> seniors,
            Integer seniorId,
            String errorIfInvalid
    ) throws CommandException {
        Senior senior = findSeniorByIdOrNull(seniors, seniorId);
        if (seniorId != null && senior == null) {
            throw new CommandException(errorIfInvalid);
        }
        return senior; // may be null if seniorId is null
    }

    /**
     * Returns the {@link Caregiver} whose {@code caregiverId} matches the given id, or throws if none is found.
     *
     * @param caregivers list to search; must not be {@code null}
     * @param caregiverId id to match; must not be {@code null}
     * @param errorIfInvalid error message for the thrown exception if no match is found; must not be {@code null}
     * @return the matching {@link Caregiver}
     * @throws CommandException if {@code caregiverId} is not found in {@code caregivers}
     */
    public static Caregiver findCaregiverById(
            List<Caregiver> caregivers,
            Integer caregiverId,
            String errorIfInvalid
    ) throws CommandException {
        Caregiver c = findCaregiverByIdOrNull(caregivers, caregiverId);
        if (c == null) {
            throw new CommandException(errorIfInvalid);
        }
        return c;
    }

    /**
     * Validates an optional {@code caregiverId}. If {@code caregiverId} is {@code null}, returns {@code null}.
     * If non-null, returns the matching {@link Caregiver} or throws if no match is found.
     *
     * @param caregivers     list to search; must not be {@code null}
     * @param caregiverId    optional id to validate; may be {@code null}
     * @param errorIfInvalid error message for the thrown exception if {@code caregiverId} is non-null but not found;
     *                       must not be {@code null}
     * @return the matching {@link Caregiver} when {@code caregiverId} is non-null; otherwise {@code null}
     * @throws CommandException if {@code caregiverId} is non-null and not found in {@code caregivers}
     */
    public static Caregiver validateOptionalCaregiverId(
            List<Caregiver> caregivers,
            Integer caregiverId,
            String errorIfInvalid
    ) throws CommandException {
        Caregiver caregiver = findCaregiverByIdOrNull(caregivers, caregiverId);
        if (caregiverId != null && caregiver == null) {
            throw new CommandException(errorIfInvalid);
        }
        return caregiver; // may be null if caregiverId is null
    }
}
