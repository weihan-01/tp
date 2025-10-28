package seedu.address.commons.util;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Caregiver;
import seedu.address.model.person.Senior;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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

    public static Senior findSeniorByIdOrNull(
            List<Senior> seniors,
            Integer seniorId
    ) {
        return findFirstByIdOrNull(seniors, seniorId, Senior::getSeniorId);
    }

    public static Caregiver findCaregiverByIdOrNull(
            List<Caregiver> caregiver,
            Integer caregiverId
    ) {
        return findFirstByIdOrNull(caregiver, caregiverId, Caregiver::getCaregiverId);
    }

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
