package game.tiles.units.player;
import game.tiles.units.Unit;
import game.callBack.MessageCallback;
import game.tiles.units.enemies.Enemy;


import java.util.List;


public class Mage extends  Player  {
        private int manapool;
        private int currentMana;
        private int manaCost;
        private int spellPower;
        private int hitsCount;
        private int abilityRange;


        public Mage(String name, int healthPool , int attack, int defense, int manapool, int manaCost, int spellPower, int hitsCount, int abilityRange) {
            super(name, healthPool, attack, defense);
            this.manapool = manapool;
            this.manaCost = manaCost;
            this.spellPower = spellPower;
            this.currentMana = manapool / 4;
            this.hitsCount = hitsCount;
            this.abilityRange = abilityRange;
        }
    public void setCurrentMana(int mana) {
        //just for the tests
        this.currentMana = Math.max(0, Math.min(mana, this.manapool));
    }

    @Override
    public void castAbility(List<Enemy> en) {
        if (this.currentMana < this.manaCost) {
            MessageCallback MCB = this.messageCallback;
            String name = this.getName();
            MCB.send(name + "  tried to cast Blizzard but there was not enough mana: " + this.currentMana + "/" + this.manaCost);
        } else {
            this.messageCallback.send(this.getName() + " cast Blizzard");
            this.currentMana = this.currentMana - this.manaCost;
            int hits = 0;

            for(List<Enemy> enemiesAbilityRange = this.enemiesAtAbilityRange(en, this.abilityRange); hits < this.hitsCount & !enemiesAbilityRange.isEmpty(); ++hits) {
                int i = (int)(Math.random() * (double)enemiesAbilityRange.size());
                Enemy e = enemiesAbilityRange.get(i);
                this.attackingWithAbility(this.spellPower, e, e.rollDefense());
                if (e.isDead()) {
                    enemiesAbilityRange.remove(e);
                }
            }
        }

    }
    int getManapool() {
        return currentMana;
    }
    @Override
    public void OnGameTick() {
        int manaAfterRegen = this.currentMana + this.plevel;
        this.currentMana = Math.min(this.manapool, manaAfterRegen);
    }

    private int getAbilityRange() {
        return abilityRange;
    }

    public void levelUp() {
            super.levelUp();
            manapool += (plevel * 25);
            currentMana = Math.min(currentMana +( manapool / 4), manapool);
            spellPower += (plevel * 10);
        MessageCallback messageCallback = this.messageCallback;
        String name = this.getName();
        messageCallback.send(name + " reached level " + (this.getPLevel() ) + " +" + this.health + " Health, +" + this.attack + " -Attack, +" + this.defense + " -Defense" + this.manapool + " -Maximum Mana " + this.spellPower + " -Spell Power" + this.currentMana+ "- Current Mana");
        }




        @Override
        public String describe() {
            return String.format("%s\t\tMana: %s/%s \t\tSpell Power: %d", super.describe(), this.currentMana, this.manapool, this.spellPower);
        }




    }




