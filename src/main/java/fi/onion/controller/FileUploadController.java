package fi.onion.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fi.onion.bgtask.ParseLogTask;

@Controller
public class FileUploadController {
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file) { 
		ApplicationContext context = new ClassPathXmlApplicationContext("servlet-context.xml");
        
		/*
		 * Handle the file information
		 */
	    ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");		
	    //taskExecutor.execute(new TestTask("Thread 1"));
	    //taskExecutor.execute(new TestTask("Thread 2"));
	    taskExecutor.execute(new ParseLogTask(file));
		
		
		
		/*
		 * Start adding all the information to DB
		 */
	    /*
        PlayerDAO playerDAO = (PlayerDAO) context.getBean("playerDAO");
        Player player = new Player(name);
        playerDAO.insert(player);
        
        Player player1 = playerDAO.findByPlayerId(1);
        */
	    
	    // redirect the user back to the front page (or the place where the file was uploaded)
        return "redirect:/";
        
        /*
		if (!file.isEmpty()) {
        	System.out.println(file.getOriginalFilename());
            return file.getOriginalFilename();
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
        */
    }
}
