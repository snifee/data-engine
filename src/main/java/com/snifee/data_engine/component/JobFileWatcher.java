package com.snifee.data_engine.component;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.*;

@Component
public class JobFileWatcher implements CommandLineRunner {

    @Value("${nio.pathToDir}")
    private String pathToDir;

    @Value("${nio.fileName}")
    private String fileName;

    @Autowired
    private JobLauncher launcher;

    @Autowired
    @Qualifier("jobExportTransaction")
    private Job job;

    final JobParameters parameter = new JobParametersBuilder().toJobParameters();


    @Override
    public void run(String... args) throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("/Users/arfalr/Documents");

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE
        );

        WatchKey key;
        while((key = watchService.take()) != null){
            for (WatchEvent<?> event: key.pollEvents()){
                if (event.kind()==StandardWatchEventKinds.ENTRY_CREATE
                        && event.context().toString().equals(fileName)
                ){
                    System.out.println(event.kind().toString()+"|"+event.context().toString());
                }
            }
            key.reset();
        }
    }
}
