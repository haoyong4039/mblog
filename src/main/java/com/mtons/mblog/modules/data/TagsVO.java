package com.mtons.mblog.modules.data;

public class TagsVO
{
    private int id;

    private String name;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override public String toString()
    {
        return "TagsVO{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
