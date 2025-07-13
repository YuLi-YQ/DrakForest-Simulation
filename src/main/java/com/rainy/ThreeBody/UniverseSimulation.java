package com.rainy.ThreeBody;

import java.util.List;

import com.rainy.tool.Point;

public class UniverseSimulation {
	
	private final UniverseMediator mediator;
    private final ResourceAllocator resourceAllocator;
    private final List<Civilization> civilizations;
    private final AttackOrder attackOrder;
    
    
    private long alive;
    private long exposed;
    
    
    public UniverseSimulation() {
        this.mediator = new UniverseMediator();
        
        CivilizationFactory factory = new CivilizationFactory(mediator);
        this.civilizations = factory.createCivilizations(SimulationConfig.CIVILIZATION_COUNT);
        
        mediator.updateCivilizationList(civilizations);
        
        this.attackOrder = new AttackOrder(mediator.getCivilizationMap());
        
        this.resourceAllocator = new ResourceAllocator();
    }
    
    public void start() {
    	
    	LogService.logStart();
        for (int step = 1; step <= SimulationConfig.TIME_STEPS ; step++) {
        	
            // Stage 0: Random exposure
            mediator.exposeRandomCivilizations();
            
            // Stage 1: Civilization randomly move
            moveCivilizations(step);
            
            // Stage 2: Resource allocation and development
            resourceAllocator.allocateResources(civilizations);
            civilizationsDevelop();
            
            // Stage 3: Attack exposed civilizations
            mediator.processAttacks(step, attackOrder);
            
            // Update exposure probability
            mediator.updateExposureProbability();
            
            // Log progress
            alive = mediator.countAliveCivilizations();
            exposed = mediator.countExposedCivilizations();
            LogService.logStep(step, alive, exposed);
            
            // It's old implements.
            // If still has attack orders,start a new step
            // if (!mediator.isAttackDone())
            	//continue;
            
            // Termination conditions
            if (alive == 0) {
                LogService.logInfo("All civilizations destroyed at step " + step);
                break;
            }
            
            if (alive == 1) {
                Civilization winner = mediator.getAliveCivilizations().get(0);
                LogService.logInfo("The final winner at step " + step);
                LogService.logWinner(winner);
                break;
            }
        }
        
        // Final report
        LogService.logResults(mediator.getAliveCivilizations());
    }
	
    private void civilizationsDevelop() {
    	civilizations.parallelStream().forEach(civ -> {
            if (!civ.isDestroyed()) {
                civ.develop();
                if (civ.getResources() < 0) {
                    civ.destroy();
                    LogService.logEvent("Civilization resource depleted", civ);
                }
            }
        });
    }
    
    
    
    private void moveCivilizations(int step) {
        for (Civilization civ : civilizations) {
            if (!civ.isDestroyed() && RandomService.nextDouble() < SimulationConfig.MOVE_PROBABILITY) {
                var current = civ.getLocation();
                int dx = RandomService.nextInt(SimulationConfig.MAX_MOVE_DISTANCE * 2) - 
                         SimulationConfig.MAX_MOVE_DISTANCE;
                int dy = RandomService.nextInt(SimulationConfig.MAX_MOVE_DISTANCE * 2) - 
                         SimulationConfig.MAX_MOVE_DISTANCE;
                int dz = RandomService.nextInt(SimulationConfig.MAX_MOVE_DISTANCE * 2) - 
                         SimulationConfig.MAX_MOVE_DISTANCE;
                
                // var newLocation = Point.valueOf(
                 int newX = clamp(current.getX() + dx, 0, SimulationConfig.UNIVERSE_SIZE_X);
                 int newY = clamp(current.getY() + dy, 0, SimulationConfig.UNIVERSE_SIZE_Y);
                 int newZ = clamp(current.getZ() + dz, 0, SimulationConfig.UNIVERSE_SIZE_Z);
                //);
                
                civ.setLocation(Point.valueOf(newX, newY, newZ));
                //LogService.logEvent("Civilization moved", civ);
            }
        }
    }
    
    private int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
    
    
	
	
	
	
	/*
	private static final int UNIVERSESIZE_X = 100000;
	
	private static final int UNIVERSESIZE_Y = 100000;
	
	private static final int UNIVERSESIZE_Z = 100000;
	
	private static final int CIVI_COUNT = 1000;
	
	private static final long RESOURCES = 1_000_000_000_000L;
	
	private static final double EXPOSED_PROBABILITY = 0.01;
	
	private static final int TIME_STEPS = 10000;
	
	private final Logger log = Logger.getGlobal();
	
	private final Random rand = new Random();
	
	private final Civilization[] universe =
			new Civilization[CIVI_COUNT];
	
	private long leftResources = (long) (RESOURCES * 0.9);
	
	double dynamicExposureProb;
	
	public UniverseSimulation() throws SecurityException, IOException {
		var temp = new FileHandler("log.txt", true);
		temp.setFormatter(new SimpleFormatter());
		log.addHandler(temp);
		
		//Initialize 
		for( int count = 1 ; count <= CIVI_COUNT ;  count++) {
			int[] x = RandomGenerator.generateUniqueRandom(CIVI_COUNT, 0, UNIVERSESIZE_X);
			int[] y = RandomGenerator.generateUniqueRandom(CIVI_COUNT, 0, UNIVERSESIZE_Y);
			int[] z = RandomGenerator.generateUniqueRandom(CIVI_COUNT, 0, UNIVERSESIZE_Z);
			long[] r = RandomGenerator.generateNonNegativeLong(CIVI_COUNT, (long) (RESOURCES * 0.1));
			
			
			universe[count - 1] = Civilization.newInstance(count,
					Point.valueOf(x[count - 1], y[count - 1], z[count - 1]), r[count - 1]);
			
		}
		
		
		
	}
	
	public long allowResources(Civilization c) {
		 if(c.isDestroyed())
			 return 0;
		 long aliveCount = Stream.of(universe)
			        .filter(civ -> !civ.isDestroyed())
			        .count();
			    
		 double weight = Math.log1p(c.getLevel()) / 
			     			Stream.of(universe)
			            .filter(civ -> !civ.isDestroyed())
			            .mapToDouble(civ -> Math.log1p(civ.getLevel()))
			            .sum();
			    
		 double allocationRatio = 0.01 * Math.sqrt(aliveCount);
		 long allocation = (long)(leftResources * allocationRatio * weight);
		 allocation = Math.min(allocation, leftResources);
		 leftResources -= allocation;
		 return allocation;
	}
	
	public void start() {
		//Start Simulation
				for (int step = 1 ; step <= TIME_STEPS ; step++) {
					
					var toDestroy = new HashSet<Civilization>();
					
					//Stage 1: Randomly Expose incident.
					for(var each : universe)
						if(!each.isDestroyed() && !each.isExposed())
							if(rand.nextDouble() < dynamicExposureProb)
								{
									each.setExposed(true);
									log.info(each + "\nHad exposed.");
								}
					
					//Stage 2: Technology Develop.
					for(var each : universe) {
						
						each.setResourses(each.getResourses() + allowResources(each));
						
						each.develop();
						if(each.getResourses() < 0) {
							each.setDestroyed(true);
							log.info("Run out of resources result in destroyed.:\n" + each);
						}

					}

					
					
					
					
					//Stage 3: High Technology Civilization try destroy exposed civilization.
					for(var target : universe) {
						if(target.isDestroyed() || !target.isExposed())
							continue;
						for(var hunter : universe) {
							if(hunter.isDestroyed() || hunter == target)
								continue;
							if(hunter.distance(target) <= hunter.getStrikeRange()
									&& hunter.attack(target)) {
								toDestroy.add(target);
								log.info("Civi#" + target.getID() + " was destroyed by Civi#" + 
										hunter.getID() + ".\n");
								log.info(target + "\nHad been destroyed.");
							}
								
						}
					}
					
					for(var target : toDestroy)
						target.setDestroyed(true);

					
					long alive = Stream.of(universe).filter(c -> !c.isDestroyed()).count();
					
					long exposed = Stream.of(universe).filter(c -> c.isExposed()).count();
					
					dynamicExposureProb = EXPOSED_PROBABILITY * 
						    (1 + 5*(1 - alive/(double)CIVI_COUNT));
					
					
					log.info("Time Step:" + step + ",still alive civilizations:" + alive + 
							",had exposed civilizations:" + exposed);
					
					if(alive == 0) {
						log.info("All civilizations had been destoryed.");
						break;
					}
					
					if(alive == 1) {
						log.info("The final WINNER has bornt.");
						break;
					}
					
				}
				long alive = Stream.of(universe).filter(c -> !c.isDestroyed()).count();
				
				log.info("Simulation stop.Result(Still Alive Count):" + alive + "\n");
				
				Stream.of(universe).filter(c -> !c.isDestroyed()).forEach(c -> log.info(c.toString()));
				
	}
	
	*/
    
    
    
    
    
    
    
    
    
    
    
}
