package com.rainy.ThreeBody;

import com.rainy.tool.Point;
/**
 * An abstract of universes' civilizations.
 * @author Rainy
 * @date 2025/7/13 -> 2.0
 * @version 3.0
 * @history
 * 2025/7/8 -> 1.1
 * 2025/7/9 -> 1.2
 * 2025/7/12 -> 1.3
 * */
public class Civilization {
	
	private final int id;
	
    private long resources;
    
    private int population = 100000;
    
    private Point location;
    
    private Point oldLocation;
    
    private int level;
    
    private CivilizationState state;
    
    private final UniverseMediator mediator;
    
    public Civilization(int id, Point location, long resources, UniverseMediator mediator) {
        
    	this.id = id;
        this.location = location;
        this.resources = resources;
        this.mediator = mediator;
        this.state = new HiddenState();
        
        // Initialize level based on resources and population
        level = (int) (resources / population * 0.8);
        if (level == 0) level = 72;
    }
    
    // State transition methods
    public void expose() {
        if (!(state instanceof DestroyState)) {
            state = new ExposedState();
            mediator.onCivilizationExposed(this);
        }
    }
    
    public void destroy() {
        if (!(state instanceof DestroyState)) {
            state = new DestroyState();
            mediator.onCivilizationDestroyed(this);
        }
    }
    
    // Development logic (now in separate method)
    void performDevelopment() {
        double exposureProb = SimulationConfig.TECH_EXPOSED_PROB * Math.log1p(level);
        if (RandomService.nextDouble() < exposureProb) {
            level += 1000 + RandomService.nextInt(5000);
            resources -= population * 0.1;
        }
        
        level += 50 + RandomService.nextInt(100);
        
        double carryingCapacity = resources / (population + 1) * 1000;
        double growthRate = SimulationConfig.BASE_GROWTH_RATE * 
                           (1 - population / (carryingCapacity + 1));
        population += (int)(population * growthRate);
        
        long consumption = (long)(population * SimulationConfig.RESOURCE_CONSUMPTION_RATE);
        resources -= consumption;
    }
    
    public void develop() {
        state.develop(this);
    }
    
    public boolean attack(Civilization target) {
        if (!state.isVisible() || state instanceof DestroyState) return false;
        
        double techAdvantage = (this.level - target.level) / 
                              (double) (this.level + target.level + 1);
        
        double distance = this.location.distance(target.location);
        double distanceFactor = 1 / (1 + distance * 0.0001);
        
        double successProbability = Math.max(0, techAdvantage * 2) * distanceFactor;
        
        return RandomService.nextDouble() < successProbability;
    }
    
    public double getStrikeRange() {
        return Math.sqrt(level) * 1000;
    }
    
    public double distance(Civilization other) {
        return this.location.distance(other.location);
    }
    
    // Getters
    public int getId() { return id; }
    public long getResources() { return resources; }
    public int getLevel() { return level; }
    public Point getLocation() { return location; }
    public boolean isDestroyed() { return state instanceof DestroyState; }
    public boolean isExposed() { return state instanceof ExposedState; }
    public int getPopulation() { return population; }
    
    // Setters with mediator notification
    public void setResources(long resources) { 
        this.resources = resources; 
    }
    
    public void setLocation(Point location) {
        oldLocation = this.location;
        this.location = location;
        mediator.onCivilizationMoved(this, oldLocation);
    }
    
    public boolean hasMoved() {
        return !location.equals(oldLocation);
    }
    
    @Override
    public String toString() {
        return String.format("Civi#%d [Tech:%d, Pop:%,d, Res:%,d] [%s] @ %s", 
                id, level, population, resources, state.getStatus(), location);
    }
    
    public String toCompactString() {
        return String.format("Civi#%d [Lvl:%d, Pop:%,d, Res:%,d] %s", 
            id, level, population, resources, state.getStatus());
    }
    
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	private static final double STRIKE_THRESHOLD = 0.6;
	
	private static final double TECH_EXPOSED_PROB = 0.01;
	
	private static final Random rand = new Random();
	
	private static final double BASE_GROWTH_RATE = 0.02;
	
	private static final double RESOURCE_CONSUMPTION_RATE = 0.0001;
	
	private static final double TECH_BOOST_MULTIPLIER = 1.3;
	
	private static final double TECH_DEVELOP_MULTIPLIER = 1.05;
	
	private static final double POPULATION_RESOURCE_RATIO = 0.0001;

	private static final double MAX_LEVEL = 1_000_000;
	
	private static final double LEVEL_DECAY_FACTOR = 0.99;
	
	private final int ID;
	
	private long resources;
	
	private int population = 100000;
	
	private Point location = Point.ORIGIN;
	
	//private TechLevel level = TechLevel.PRIMARY;
	private int level;
	
	private boolean isExposed = false;
	
	private boolean isDestroyed = false;
	
	
	
	
	@Override
	public String toString() {
		return "Civi#" + ID + "\n[TechLevel: "
				+ level + "]\n[" + 
				(isExposed ? "Exposed" : "Hidden")
				+ ", Alive:" + !isDestroyed + "]\n" +
				"[Resources: " + resources + "]\n"
				+ location;
	}
	
	public static Civilization newInstance(int iD, Point location, long resources) {
		return new Civilization(iD, location, resources);
	}
	
	public double distance(Civilization other) {
		return this.location.distance(other.location);
	}
	
	
	public final boolean isExposed() {
		return isExposed;
	}

	public final boolean isDestroyed() {
		return isDestroyed;
	}

	public final void Exposed() {
		isExposed = true;
	}

	public final void destroy() {
		isDestroyed = true;
	}

	public long getResourses() {
		return resources;
	}

	public final int getLevel() {
		return level;
	}

	public final int getID() {
		return ID;
	}

	public void setResourses(long resourses) {
		this.resources = resourses;
	}

	private Civilization(int iD, Point location, long resources) {
		ID = iD;
		this.location = location;
		this.resources = resources;
		level = (int) (resources / population * 0.8);
		if(level == 0)
			level = 72;
	}
	
	public boolean attack(Civilization target) {
		double techAdvantage = (this.level - target.level) / (double) (this.level + target.level + 1);
	    
	    double distance = this.location.distance(target.location);
	    double distanceFactor = 1 / (1 + distance * 0.0001);
	    
	    double successProbability = Math.max(0, techAdvantage * 2) * distanceFactor;
	    
	    return rand.nextDouble() < successProbability;
	}
	
	public void develop() {
		
		/*
		if(rand.nextDouble() < TECH_EXPOSED_PROB * level) {
			level *= 10;
			resources -= population * 0.5;
		}
		int temp = population;
		temp *= 1 + level * 0.01;
		int consumption = (int) (temp * ( 0.1 + level * 0.02));
		level *= 5;
		resources -= consumption;
		*/
		/*
	    double exposureProb = Math.min(TECH_EXPOSED_PROB * level, 0.5);
	    if(rand.nextDouble() < exposureProb) {
	        level = (int)(level * TECH_BOOST_MULTIPLIER);
	        resources -= population * POPULATION_RESOURCE_RATIO; 
	    }
	    double growthFactor = BASE_GROWTH_RATE * Math.log1p(level);
	    population = (int)Math.min(population * (1 + growthFactor), 10_000_000_000L);
	    
	    double consumptionFactor = RESOURCE_CONSUMPTION_RATE * Math.log1p(level);
	    long consumption = (long)(population * consumptionFactor);
	    resources = Math.max(0, resources - consumption);
	    
	    if(level < MAX_LEVEL) {
	        double decayedMultiplier = TECH_DEVELOP_MULTIPLIER * 
	            Math.pow(LEVEL_DECAY_FACTOR, level / 1000.0);
	        level = (int)(level * decayedMultiplier);
	    }
	    */
		/*
		double exposureProb = TECH_EXPOSED_PROB * Math.log1p(level);
	    if (rand.nextDouble() < exposureProb) {
	        level += 1000 + rand.nextInt(5000);
	        resources -= population * 0.1;
	    }
	    
	    level += 50 + rand.nextInt(100);
	    
	    double carryingCapacity = resources / (population + 1) * 1000;
	    double growthRate = BASE_GROWTH_RATE * (1 - population / (carryingCapacity + 1));
	    population += (int)(population * growthRate);
	    
	    long consumption = (long)(population * RESOURCE_CONSUMPTION_RATE);
	    resources -= consumption;
		
	}
	
	public double getStrikeRange() {
		return Math.sqrt(level) * 1000;
	}

	public Point getLocation() {
		return location;
	}
	
	
	
}
*/
