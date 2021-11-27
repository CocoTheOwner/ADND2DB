package nl.codevs.dndinventory.inventories.interfaces;

import nl.codevs.dndinventory.data.Dice;

import java.util.function.Function;

public interface ILevel {

    /**
     * Get the current amount of experience.
     * @return the current amount of experience
     */
    int getExperience();

    /**
     * Add experience.
     * @param experience the experience to add
     */
    void addExperience(final int experience);

    /**
     * Get the current character's class.
     * @return the current character's class
     */
    CharacterClass getCharacterClass();

    /**
     * Get the current character's actual level.
     * @return the current character's actual level
     */
    int getActualLevel();

    /**
     * Set the actual level.
     *
     * @param actualLevel the new actual level
     */
    void setActualLevel(final int actualLevel);

    /**
     * Get the amount of levels that can be trained.
     * @return the amount of levels that can be trained.
     */
    default int getTrainableLevels() {
        return getPotentialLevel() - getActualLevel();
    }

    /**
     * Get character level.
     * @return the current character's level
     */
    default int getPotentialLevel() {
        int[] xps = switch (getCharacterClass()) {
            case FIGHTER -> XP_FIGHTER;
            case PALADIN, RANGER -> XP_PALADIN_RANGER;
            case MAGE, SPECIALIST -> XP_MAGE_SPECIALIST;
            case CLERIC -> XP_CLERIC;
            case DRUID -> XP_DRUID;
            case THIEF, BARD -> XP_THIEF_BARD;
        };
        for (int i = 0; i < xps.length; i++) {
            if (xps[i] < getExperience()) {
                continue;
            }
            return i;
        }
        throw new RuntimeException(
                "Potential level cannot be found for class/xp: "
                        + getCharacterClass() + "/" + getExperience()
                        + " because it exceeds " + xps[xps.length - 1]);
    }

    /**
     * Convert warrior level into dice.
     */
    Function<Integer, Dice> WARRIOR_LEVEL_DICE = (i) -> i < 10 ? new Dice(10, i, 0) : new Dice(10, 9, (i - 9)  * 3);
    Function<Integer, Dice> WIZARD_LEVEL_DICE =  (i) -> i < 11 ? new Dice(4, i, 0)  : new Dice(4, 10, (i - 10) * 1);
    Function<Integer, Dice> PRIEST_LEVEL_DICE =  (i) -> i < 10 ? new Dice(8, i, 0)  : new Dice(8,  9, (i - 9)  * 2);
    Function<Integer, Dice> ROGUE_LEVEL_DICE =   (i) -> i < 11 ? new Dice(6, i, 0)  : new Dice(6, 10, (i - 10) * 2);

    /**
     * The level-up to the current {@link #getPotentialLevel()}.
     * @return {@link Dice} with the dice details for level-up health
     */
    default Dice getHitPointDice() {
        return (switch (ClassGroup.of(getCharacterClass())) {
            case WARRIOR -> WARRIOR_LEVEL_DICE;
            case WIZARD  -> WIZARD_LEVEL_DICE;
            case PRIEST  -> PRIEST_LEVEL_DICE;
            case ROGUE   -> ROGUE_LEVEL_DICE;
        }).apply(getPotentialLevel() - 1);
    }

    /**
     * Fighter XP.
     */
    int[] XP_FIGHTER = new int[]{
            0,          2_000,      4_000,      8_000,
            16_000,     32_000,     64_000,     125_000,
            250_000,    500_000,    750_000,    1_000_000,
            1_250_000,  1_500_000,  1_750_000,  2_000_000,
            2_250_000,  2_500_000,  2_750_000,  3_000_000
    };
    /**
     * Paladin & Ranger XP.
     */
    int[] XP_PALADIN_RANGER = new int[]{
            0,          2_250,      4_500,      9_000,
            18_000,     36_000,     75_000,     150_000,
            300_000,    600_000,    900_000,    1_200_000,
            1_500_000,  1_800_000,  2_100_000,  2_400_000,
            2_700_000,  3_000_000,  3_300_000,  3_600_000
    };
    /**
     * Mage & Specialist XP.
     */
    int[] XP_MAGE_SPECIALIST = new int[]{
            0,          2_500,      5_000,      10_000,
            20_000,     40_000,     60_000,     90_000,
            135_000,    250_000,    375_000,    750_000,
            1_125_000,  1_500_000,  1_875_000,  2_250_000,
            2_625_000,  3_000_000,  3_375_000,  3_750_000
    };
    /**
     * Cleric XP.
     */
    int[] XP_CLERIC = new int[]{
            0,          1_500,      3_000,      6_000,
            13_000,     27_500,     55_000,     110_000,
            225_000,    450_000,    675_000,    900_000,
            1_125_000,  1_350_000,  1_575_000,  1_800_000,
            2_025_000,  2_250_000,  2_475_000,  2_700_000
    };
    /**
     * Druid XP.
     */
    int[] XP_DRUID = new int[]{
            0,          2_000,      4_000,      7_500,
            12_500,     20_000,     35_000,     60_000,
            90_000,     125_000,    200_000,    300_000,
            750_000,    1_500_000,  3_000_000,  3_500_000,
            500_000,    1_000_000,  1_500_000,  2_000_000
    };
    /**
     * Thief & Bard XP.
     */
    int[] XP_THIEF_BARD = new int[]{
            0,          1_250,      2_500,      5_000,
            10_000,     20_000,     40_000,     70_000,
            110_000,    160_000,    220_000,    440_000,
            660_000,    880_000,    1_100_000,  1_320_000,
            1_540_000,  1_760_000,  1_980_000,  2_200_000
    };

    /**
     * Character class types.
     */
    enum CharacterClass {
        /**
         * Warriors, Fighter.
         */
        FIGHTER,
        /**
         * Warriors, Paladin.
         */
        PALADIN,
        /**
         * Warriors, Ranger.
         */
        RANGER,

        /**
         * Wizards Mage.
         */
        MAGE,
        /**
         * Warriors, Specialist.
         */
        SPECIALIST,

        /**
         * Priests, Cleric.
         */
        CLERIC,
        /**
         * Priests, Druid.
         */
        DRUID,

        /**
         * Rogue, Thief.
         */
        THIEF,

        /**
         * Rogue, Bard.
         */
        BARD

    }

    enum ClassGroup {
        /**
         * Warriors.
         */
        WARRIOR,
        /**
         * Wizards.
         */
        WIZARD,
        /**
         * Priests.
         */
        PRIEST,
        /**
         * Rogues.
         */
        ROGUE;

        /**
         * Get the ClassGroup belonging to the class.
         * @param characterClass character class
         * @return the ClassGroup
         */
        public static ClassGroup of(final CharacterClass characterClass) {
            return switch (characterClass) {
                case FIGHTER, RANGER, PALADIN -> WARRIOR;
                case MAGE, SPECIALIST -> WIZARD;
                case CLERIC, DRUID -> PRIEST;
                case THIEF, BARD -> ROGUE;
            };
        }
    }
}
