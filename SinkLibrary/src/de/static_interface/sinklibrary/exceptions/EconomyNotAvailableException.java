package de.static_interface.sinklibrary.exceptions;

/**
 * Author: Trojaner
 * Date: 26.09.13
 */
public class EconomyNotAvailableException extends RuntimeException
{
    public EconomyNotAvailableException()
    {
        super("Economy is not available!");
    }
}
