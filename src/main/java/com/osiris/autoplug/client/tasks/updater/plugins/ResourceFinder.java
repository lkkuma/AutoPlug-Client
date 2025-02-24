/*
 * Copyright (c) 2021-2023 Osiris-Team.
 * All rights reserved.
 *
 * This software is copyrighted work, licensed under the terms
 * of the MIT-License. Consult the "LICENSE" file for details.
 */

package com.osiris.autoplug.client.tasks.updater.plugins;


import com.osiris.autoplug.client.tasks.updater.mods.CurseForgeAPI;
import com.osiris.autoplug.client.tasks.updater.mods.InstalledModLoader;
import com.osiris.autoplug.client.tasks.updater.mods.MinecraftMod;
import com.osiris.autoplug.client.tasks.updater.mods.ModrinthAPI;
import com.osiris.autoplug.client.tasks.updater.search.GithubSearch;
import com.osiris.autoplug.client.tasks.updater.search.JenkinsSearch;
import com.osiris.autoplug.client.tasks.updater.search.SearchResult;
import com.osiris.autoplug.client.tasks.updater.search.bukkit.BukkitSearchById;
import com.osiris.autoplug.client.tasks.updater.search.spigot.SpigotSearchByAuthor;
import com.osiris.autoplug.client.tasks.updater.search.spigot.SpigotSearchById;
import com.osiris.autoplug.client.tasks.updater.search.spigot.SpigotSearchByName;

public class ResourceFinder {

    /**
     * If the spigot/bukkit id is not given this type of search
     * based on the plugins' name and author will be executed.
     */
    public SearchResult findUnknownPlugin(MinecraftPlugin plugin) {
        // Do spigot search by name
        SearchResult sr = new SpigotSearchByName().search(plugin);

        if (sr == null || sr.getResultCode() == 2 || sr.getResultCode() == 3) {
            //Couldn't find author or resource via first search
            //Do alternative search:
            sr = new SpigotSearchByAuthor().search(plugin);
        }

        plugin.setPremium(sr.isPremium);
        sr.plugin = plugin;
        return sr;
    }

    /**
     * If the modrinth/bukkit id is not given this type of search
     * based on the mods' name and author will be executed.
     */
    public SearchResult findByModrinthOrCurseforge(InstalledModLoader modLoader, MinecraftMod mod, String mcVersion, boolean checkNameForModLoader) {
        // Do spigot search by name
        SearchResult sr = new ModrinthAPI().searchUpdate(modLoader, mod, mcVersion);

        if (sr == null || sr.getResultCode() == 2 || sr.getResultCode() == 3) {
            //Couldn't find author or resource via first search
            //Do alternative search:
            sr = new CurseForgeAPI().searchUpdate(modLoader, mod, mcVersion, checkNameForModLoader);
        }

        sr.mod = mod;
        return sr;
    }

    public SearchResult findPluginBySpigotId(MinecraftPlugin plugin) {
        SearchResult sr = new SpigotSearchById().search(plugin);
        plugin.setPremium(sr.isPremium);
        sr.plugin = plugin;
        return sr;
    }

    public SearchResult findPluginByBukkitId(MinecraftPlugin plugin) {
        SearchResult sr = new BukkitSearchById().search(plugin);
        plugin.setPremium(sr.isPremium);
        sr.plugin = plugin;
        return sr;
    }

    public SearchResult findModByModrinthId(InstalledModLoader modLoader, MinecraftMod mod, String mcVersion) {
        SearchResult sr = new ModrinthAPI().searchUpdate(modLoader, mod, mcVersion);
        sr.mod = mod;
        return sr;
    }

    public SearchResult findModByCurseforgeId(InstalledModLoader modLoader, MinecraftMod mod, String mcVersion, boolean checkNameForModLoader) {
        SearchResult sr = new CurseForgeAPI().searchUpdate(modLoader, mod, mcVersion, checkNameForModLoader);
        sr.mod = mod;
        return sr;
    }

    public SearchResult findByGithubUrl(MinecraftMod mod) {
        SearchResult sr = new GithubSearch().search(mod.githubRepoName, mod.githubAssetName, mod.getVersion());
        sr.mod = mod;
        return sr;
    }

    public SearchResult findByJenkinsUrl(MinecraftMod mod) {
        SearchResult sr = new JenkinsSearch().search(mod.jenkinsProjectUrl, mod.jenkinsArtifactName, mod.jenkinsBuildId);
        sr.mod = mod;
        return sr;
    }

    public SearchResult findByGithubUrl(MinecraftPlugin plugin) {
        SearchResult sr = new GithubSearch().search(plugin.getGithubRepoName(), plugin.getGithubAssetName(), plugin.getVersion());
        plugin.setPremium(sr.isPremium);
        sr.plugin = plugin;
        return sr;
    }

    public SearchResult findByJenkinsUrl(MinecraftPlugin plugin) {
        SearchResult sr = new JenkinsSearch().search(plugin.getJenkinsProjectUrl(), plugin.getJenkinsArtifactName(), plugin.getJenkinsBuildId());
        plugin.setPremium(sr.isPremium);
        sr.plugin = plugin;
        return sr;
    }
}
