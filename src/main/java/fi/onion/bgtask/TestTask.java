package fi.onion.bgtask;

public class TestTask implements Runnable {
	String name;
	
	public TestTask(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		System.out.println(name + " is running");
		
		try {
			Thread.sleep(5000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(name + " is still running");
	}

}
