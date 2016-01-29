/*   Copyright 2016 Karlsruhe Institute of Technology (KIT)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.kit.scc.ldap;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

import edu.kit.scc.dao.GroupDAO;
import edu.kit.scc.dto.GroupDTO;

public class LdapGroupDAO implements GroupDAO {
	private static final Logger log = LoggerFactory.getLogger(LdapGroupDAO.class);

	private LdapTemplate ldapTemplate;

	private String groupBase;

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public void setGroupBase(String groupBase) {
		this.groupBase = groupBase;
	}

	@Override
	public List<GroupDTO> getAllGroups() {
		return ldapTemplate.search(groupBase, "(objectclass=posixGroup)", new LdapGroupAttributeMapper());

	}

	@Override
	public List<GroupDTO> getGroupDetails(String commonName) {
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectclass", "posixGroup")).and(new EqualsFilter("cn", commonName));
		log.debug("LDAP query {}", andFilter.encode());

		return ldapTemplate.search("", andFilter.encode(), new LdapGroupAttributeMapper());
	}

	@Override
	public void insertGroup(GroupDTO groupDTO) {
		BasicAttribute posixGroupBasicAttribute = new BasicAttribute("objectclass");
		posixGroupBasicAttribute.add("posixGroup");

		Attributes posixGroupAttributes = new BasicAttributes();
		posixGroupAttributes.put(posixGroupBasicAttribute);
		posixGroupAttributes.put("cn", groupDTO.getCommonName());
		posixGroupAttributes.put("gidNumber", String.valueOf(groupDTO.getGidNumber()));

		LdapName newGroupDN = LdapUtils.emptyLdapName();
		try {
			newGroupDN = new LdapName(groupBase);
			newGroupDN.add("cn=" + groupDTO.getCommonName());
			log.debug(newGroupDN.toString());
			ldapTemplate.bind(newGroupDN, null, posixGroupAttributes);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateGroup(GroupDTO groupDTO) {
		BasicAttribute posixGroupBasicAttribute = new BasicAttribute("objectclass");
		posixGroupBasicAttribute.add("posixGroup");

		Attributes posixGroupAttributes = new BasicAttributes();
		posixGroupAttributes.put(posixGroupBasicAttribute);
		posixGroupAttributes.put("cn", groupDTO.getCommonName());
		posixGroupAttributes.put("gidNumber", String.valueOf(groupDTO.getGidNumber()));

		LdapName groupDN = LdapUtils.emptyLdapName();
		try {
			groupDN = new LdapName(groupBase);
			groupDN.add("cn=" + groupDTO.getCommonName());
			log.debug(groupDN.toString());
			ldapTemplate.bind(groupDN, null, posixGroupAttributes);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteGroup(GroupDTO groupDTO) {
		LdapName groupDN = LdapUtils.emptyLdapName();
		try {
			groupDN = new LdapName(groupBase);
			groupDN.add("cn=" + groupDTO.getCommonName());
			log.debug(groupDN.toString());
			ldapTemplate.unbind(groupDN);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMember(GroupDTO groupDTO, String memberUid) {
		ModificationItem[] modificationItems = new ModificationItem[] {
				new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("memberUid", memberUid)) };
		LdapName groupDN = LdapUtils.emptyLdapName();
		try {
			groupDN = new LdapName(groupBase);
			groupDN.add("cn=" + groupDTO.getCommonName());
			log.debug(groupDN.toString());
			ldapTemplate.modifyAttributes(groupDN, modificationItems);
		} catch (InvalidNameException e) {
			e.printStackTrace();
		}
	}
}
