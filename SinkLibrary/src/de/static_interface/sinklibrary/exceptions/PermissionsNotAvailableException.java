package de.static_interface.sinklibrary.exceptions;

/**
 * Author: Trojaner
 * Date: 26.09.13
 */
public class PermissionsNotAvailableException extends RuntimeException
{
    public PermissionsNotAvailableException()
    {
        super("Permissions are not available!");
    }
}
