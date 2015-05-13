package com.sweetlab.sweetspot.modifiers;

/**
 * Factory to create CollectionModifiers.
 */
public class ModifierFactory {

    /**
     * Will create a modifier specified by type.
     *
     * @param modifierType The modifierType.
     * @return A collection modifier.
     */
    public static CollectionModifier create(ModifierType modifierType) {
        switch (modifierType) {
            case NONE:
                return new NoModifier();
            case DAY:
                return new DayDividerModifier();
            default:
                throw new RuntimeException("wtf create");
        }
    }
}
