package com.ednevnik.security;

import java.util.HashMap;
import java.util.Map;

import com.ednevnik.entities.EUlogaEntity;

public class Views {
	
	public static class PublicView{};
	public static class UcenikView extends PublicView{};
	public static class RoditeljView extends UcenikView{};
	public static class NastavnikView extends RoditeljView{};
	public static class AdminView extends NastavnikView{};

	
	public static final Map<EUlogaEntity, Class> MAPPING = new HashMap();
	
	static {
		MAPPING.put(EUlogaEntity.ROLE_UCENIK, UcenikView.class);
		MAPPING.put(EUlogaEntity.ROLE_RODITELJ, RoditeljView.class);
		MAPPING.put(EUlogaEntity.ROLE_NASTAVNIK, NastavnikView.class);
		MAPPING.put(EUlogaEntity.ROLE_ADMINISTRATOR, AdminView.class);
	}
}		
