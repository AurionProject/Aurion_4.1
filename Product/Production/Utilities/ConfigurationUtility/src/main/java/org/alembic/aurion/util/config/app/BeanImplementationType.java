package org.alembic.aurion.util.config.app;

/**
 *
 * @author Neil Webb
 */
enum BeanImplementationType
{
    NOOP("noop"),
    JAVA("java"),
    WSSECURED("wssecured"),
    WSUNSECURED("wsunsecured");

    private String name = null;

    BeanImplementationType(String name)
    {
        this.name = name;
    };

    public String implementationType()
    {
        return name;
    }
}
