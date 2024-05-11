package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class StreamService {

    private final static Logger logger = LoggerFactory.getLogger(StreamService.class);

    public int getSum() {
        logger.debug("Method getSum was invoked");
        int sum = IntStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .sum();
        return sum;
    }
}
