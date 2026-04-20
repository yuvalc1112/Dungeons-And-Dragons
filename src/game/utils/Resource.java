package game.utils;


public class Resource {
    private int max;
    private int current;

    public Resource(int max, int current) {
        this.max = max;
        this.current = current; // Initialize current to max
    }
    public int getMax() {
        return max;
    }
    public void addMax(int max){
        this.max += max;
    }
    public int getCurrent() {
        return current;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public void setCurrent(int current) {
        this.current = Math.min(current, max);
    }
    public void addCurrent(int current) {
        this.current = Math.min(this.current + current, max);
    }
    public void reduceCurrent(int current) {
        this.current = Math.max(this.current - current, 0);
    }
    public void reHelth() {
        this.current = max;
    }
    @Override
    public String toString() {
        return "Resource{" +
                "max=" + max +
                ", current=" + current +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Resource other = (Resource) obj;
        return this.max == other.max && this.current == other.current;
    }

}
