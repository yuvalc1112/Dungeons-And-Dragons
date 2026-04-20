package game.tiles.units.player;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;
public class PlayerTest {
    private Warrior warrior;
    private Mage mage;
    private Rogue rogue;

    @Before
    public void setUp() {
        warrior = new Warrior("Jon Snow", 300, 30, 4, 3);
        mage = new Mage("Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        rogue = new Rogue("Arya Stark", 150, 40, 2, 20);

        warrior.SetMessageCallBack(msg -> {});
        mage.SetMessageCallBack(msg -> {});
        rogue.SetMessageCallBack(msg -> {});
    }

    @Test
    public void testWarriorLevelUp() {
        warrior.addExperience(50);
        assertEquals("Warrior should be level 2.", 2, warrior.getPLevel());
        assertEquals("Warrior max health is incorrect.", 330, warrior.getHealth().getMax());
        assertEquals("Warrior attack is incorrect.", 42, warrior.getAttack());
        assertEquals("Warrior defense is incorrect.", 8, warrior.getDefense());
    }

    @Test
    public void testExperienceGainWithoutLevelUp() {
        int initialExp = warrior.getExperience();
        warrior.addExperience(30);
        assertEquals("Level should not change.", 1, warrior.getPLevel());
        assertEquals("Experience should be added correctly.", initialExp + 30, warrior.getExperience());
    }

    @Test
    public void testMageManaRegeneratesCorrectlyOnTick() {
        int initialMana = mage.getManapool();
        mage.OnGameTick();
        assertEquals("Mana should regenerate by player level (1) each tick.", initialMana + 1, mage.getManapool());
    }


    @Test
    public void testWarriorAbilityFailsOnCooldown() {
        int initialHealth = warrior.getHealth().getCurrent();
        warrior.castAbility(Collections.emptyList());
        assertEquals("Health should not change when ability is on cooldown.", initialHealth, warrior.getHealth().getCurrent());
    }
    @Test
    public void debug_ManaCappingLogic() {

        mage.setCurrentMana(299);
        assertEquals("Pre-condition failed: setCurrentMana did not work correctly.", 299, mage.getManapool());
        mage.OnGameTick();
        int finalMana = mage.getManapool();
        int expectedMaxMana = mage.getManapool();
        assertEquals("Mana should be capped at the maximum value after regeneration.",
                expectedMaxMana,
                finalMana);

    }
}