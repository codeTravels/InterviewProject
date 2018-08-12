/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.interview.reader;

import com.test.interview.model.EventEntry;
import java.io.IOException;

/**
 *
 * @author Cory
 */
public interface EventEntryProvider
{

    /**
     * Reads the next line in the file reader and creates a corresponding
     * EventEntry object.
     *
     * @return
     */
    public EventEntry next(JsonFileReader reader) throws IOException;
}
