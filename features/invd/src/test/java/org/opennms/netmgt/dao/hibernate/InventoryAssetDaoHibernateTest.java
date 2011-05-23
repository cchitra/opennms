//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.netmgt.dao.hibernate;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.inventory.OnmsInventoryAsset;
import org.opennms.netmgt.model.inventory.OnmsInventoryAssetProperty;
import org.opennms.netmgt.model.inventory.OnmsInventoryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
	InvdConfigurationExecutionListener.class,
	TemporaryDatabaseExecutionListener.class,
	DependencyInjectionTestExecutionListener.class
})
@ContextConfiguration(locations={		
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-invDatabasePopulator.xml"
})
@JUnitTemporaryDatabase(populate=true)
public class InventoryAssetDaoHibernateTest {
	@Autowired
    InventoryDatabasePopulator m_dbPopulator;
	
	@Before
	public void setUp() throws Exception {
        m_dbPopulator.populateDatabase();
    }
	
	@Test
    public void testInitialize() {
        // do nothing, just test that setUp() / tearDown() works
    }

	@Test
    public void testSaveOnmsInventoryAsset() {
        // Create a new inventory category.
        OnmsInventoryCategory invCat = getDbPopulator().getInventoryCategoryDao().findByName("Network Equipment");
        OnmsNode node = getDbPopulator().getNode1();

        OnmsInventoryAsset invAsset = new OnmsInventoryAsset( invCat, "Network Stuff", node);

        getDbPopulator().getInventoryAssetDao().save(invAsset);
        getDbPopulator().getInventoryAssetDao().flush();
        getDbPopulator().getInventoryAssetDao().clear();

        // Now pull it back up and make sure it saved.
        //Object [] args = { invAsset.getId() };
        //assertEquals(1, getJdbcTemplate().queryForInt("select count(*) from inventoryasset where id = ?", args));

        OnmsInventoryAsset invAsset2 = getDbPopulator().getInventoryAssetDao().findByAssetId(invAsset.getId());
        Assert.assertNotSame(invAsset, invAsset2);
        Assert.assertEquals(invAsset.getAssetId(), invAsset2.getAssetId());
        Assert.assertEquals(invAsset.getAssetName(), invAsset2.getAssetName());
        Assert.assertEquals(invCat.getId(), invAsset2.getCategory().getId());
        Assert.assertEquals(node.getNodeId(), invAsset2.getOwnerNode().getNodeId());
    }

	@Test
    public void testFindByAssetName() {
        Collection<OnmsInventoryAsset> assets = getDbPopulator().getInventoryAssetDao().findByName("Network Card");
        Assert.assertEquals("number of assets found", 1, assets.size());
        
        // TODO
        //OnmsInventoryAsset invAsset = getInventoryAssetDao().findByAssetId(invAsset.getId());
        //assertNotSame(invAsset, invAsset2);
        //assertEquals(invAsset.getAssetId(), invAsset2.getAssetId());
        //assertEquals(invAsset.getAssetName(), invAsset2.getAssetName());
        //assertEquals(invCat.getId(), invAsset2.getCategory().getId());
        //assertEquals(node.getNodeId(), invAsset2.getOwnerNode().getNodeId());
    }

	@Test
    public void testFindByAssetId() {
        // Fetch the one we create in DatabasePopulator
        OnmsInventoryAsset invAsset1 = getDbPopulator().getInvAsset1();
        
        // Now use the DAO to fetch the inventory asset.
        OnmsInventoryAsset invAsset2 = getDbPopulator().getInventoryAssetDao().findByAssetId(invAsset1.getId());
        Assert.assertEquals(invAsset1.getAssetId(), invAsset2.getAssetId());
        Assert.assertEquals(invAsset1.getAssetName(), invAsset2.getAssetName());
        Assert.assertEquals(invAsset1.getCategory().getId(), invAsset2.getCategory().getId());
        Assert.assertEquals(invAsset1.getOwnerNode().getNodeId(), invAsset2.getOwnerNode().getNodeId());
    }
	
	@Test
	public void testFindByNameAndNodeId() {
		OnmsInventoryAsset invAsset1 = getDbPopulator().getInvAsset1();
		
		Collection<OnmsInventoryAsset> assets = getDbPopulator().getInventoryAssetDao().findByNameAndNode(invAsset1.getAssetName(), invAsset1.getOwnerNode());
		Assert.assertEquals("Total collection size should be 1.", 1, assets.size());
		OnmsInventoryAsset invAsset2 = (OnmsInventoryAsset)assets.toArray()[0];
		
		Assert.assertEquals(invAsset1.getAssetId(), invAsset2.getAssetId());
        Assert.assertEquals(invAsset1.getAssetName(), invAsset2.getAssetName());
        Assert.assertEquals(invAsset1.getCategory().getId(), invAsset2.getCategory().getId());
        Assert.assertEquals(invAsset1.getOwnerNode().getNodeId(), invAsset2.getOwnerNode().getNodeId());
	}
	
	@Test
	public void testFindByAssetDoesNotExist() {
		OnmsInventoryAsset invAsset = getDbPopulator().getInventoryAssetDao().findByAssetId(999);
		Assert.assertNull("Asset ID 999 should not exist and the value should be null.", invAsset);
	}

	@Test
    public void testGetAssetProperties() {
        // Retrieve an asset from the populator
        OnmsInventoryAsset invAsset = getDbPopulator().getInventoryAssetDao().findByAssetId(getDbPopulator().getInvAsset1().getId());
        //OnmsInventoryAsset populatorAsset = getDbPopulator().getInvAsset1();
        
        // Now retrieve a property.
        Collection<OnmsInventoryAssetProperty> props = invAsset.getProperties();
        //Assert.assertEquals("the populator asset properties found", 2, populatorAsset.getProperties().size());
        Assert.assertEquals("number of asset properties found", 2, props.size());
    }

	public InventoryDatabasePopulator getDbPopulator() {
		return m_dbPopulator;
	}

	public void setDbPopulator(InventoryDatabasePopulator dbPopulator) {
		m_dbPopulator = dbPopulator;
	}
}