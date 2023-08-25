package com.pany.mods.entity_capturing_tool.injectedinterfaces;

import com.pany.mods.entity_capturing_tool.Helpers.ConfigHelper;

import java.io.IOException;

public interface confighandling {
    default ConfigHelper GetEndercageConfig() {return null;}
    default void GenerateConfig() {}

    //default void RetryConfig() throws IOException {}
}
