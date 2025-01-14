package me.fixeddev.commandflow.command.modifiers;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class contains the fallback modifiers for every command, if no other modifiers are found in the actual command.
 */
public class FallbackCommandModifiers extends SimpleCommandModifiers {

    public FallbackCommandModifiers() {
        super(new HashMap<>());

        modifiersByPhase.put(ModifierPhase.PRE_PARSE, new SequentialCommandModifier(new LinkedList<>()));
        modifiersByPhase.put(ModifierPhase.PRE_EXECUTE, new SequentialCommandModifier(new LinkedList<>()));
        modifiersByPhase.put(ModifierPhase.POST_EXECUTE, new SequentialCommandModifier(new LinkedList<>()));
    }

    /**
     * Adds a new modifier at the end of the list of modifiers to the specified phase.
     * <p>
     * If the modifier already set for the phase isn't a {@linkplain SequentialCommandModifier} then the
     * modifier will removed, a {@linkplain SequentialCommandModifier} will be created and the old modifier will be added
     * first.
     *
     * @param modifier The modifier to add.
     * @param phase    The phase to add the modifier to.
     */
    public void addModifier(CommandModifier modifier, ModifierPhase phase) {
        getSequential(phase).addModifier(modifier);
    }

    /**
     * Adds a new modifier at specified index of the list of modifiers to the specified phase.
     * <p>
     * If the modifier already set for the phase isn't a {@linkplain SequentialCommandModifier} then the
     * modifier will removed, a {@linkplain SequentialCommandModifier} will be created and the old modifier will be added
     * first.
     *
     * @param modifier The modifier to add.
     * @param phase    The phase to add the modifier to.
     * @param idx      The index to add the modifier at.
     */
    public void addModifier(CommandModifier modifier, ModifierPhase phase, int idx) {
        getSequential(phase).addModifier(idx, modifier);
    }

    /**
     * Removes the specified modifier from the list of modifiers, if it exists.
     * <p>
     * If the modifier being removed is the parent one, then it will be replaced with a {@linkplain SequentialCommandModifier}.
     *
     * @param modifier The modifier to remove.
     * @param phase    The phase to remove the modifier from.
     */
    public void removeModifier(CommandModifier modifier, ModifierPhase phase) {
        ModifierUtils.removeModifier(modifier, phase, modifiersByPhase);
    }

    /**
     * Removes the modifier from the specified index from the list of modifiers, if it exists.
     * <p>
     * If the parent modifier isn't a {@linkplain SequentialCommandModifier} then this method will do nothing.
     *
     * @param idx   The index of the modifier to remove.
     * @param phase The phase to remove the modifier from.
     */
    public void removeModifier(int idx, ModifierPhase phase) {
        CommandModifier phaseModifier = modifiersByPhase.get(phase);

        if (phaseModifier instanceof SequentialCommandModifier) {
            SequentialCommandModifier seqModifier = (SequentialCommandModifier) phaseModifier;

            seqModifier.removeModifier(idx);
        }
    }

    /**
     * Sets the main modifier of the specified phase.
     *
     * @param modifier The modifier to set.
     * @param phase    The phase to set the modifier to.
     */
    public void setModifier(CommandModifier modifier, ModifierPhase phase) {
        modifiersByPhase.put(phase, modifier);
    }

    private SequentialCommandModifier getSequential(ModifierPhase phase) {
        return ModifierUtils.getSequential(phase, modifiersByPhase);
    }

}
