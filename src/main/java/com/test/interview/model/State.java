/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.interview.model;

/**
 *
 * @author Cory
 */
public enum State
{
    STARTED("STARTED"),
    FINISHED("FINISHED");
    private final String value;

    private State(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
