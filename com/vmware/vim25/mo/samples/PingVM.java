/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

* Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package com.vmware.vim25.mo.samples;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.samples.vm.MigrateVM;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
public class PingVM 
{
	public void checkStatus() throws IOException, InterruptedException {
		int alive = 0;
		int aliveVM = 0;
		int dead = 0;
		int deadVM = 0;
		ArrayList<String> liveHosts = new ArrayList<String>();
		
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.210/sdk"), "administrator", "12!@qwQW", true);
		Folder rootFolder = si.getRootFolder();
		
		System.out.println("\n============ Ping Hosts ============");
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		ManagedEntity[] VM = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");

		for(int i=0; i<hosts.length; i++) {
			//HostSystem vmhosts = (HostSystem) hosts[i] ;
			//HostSystemConnectionState vmcs = vmhosts.getRuntime().getConnectionState();
			
			//if (vmcs.connected == vmhosts.getRuntime().getConnectionState()) {
				//final String hostname = hosts[i].getName();
				//ServiceInstance hi = new ServiceInstance(new URL("https://"+hostname+"/sdk"), "root", "12!@qwQW", true);
				//Folder hostFolder = hi.getRootFolder();
				//ManagedEntity[] vms2 = new InventoryNavigator(hostFolder).searchManagedEntities(
						//new String[][] { {"VirtualMachine", "name" }, }, true);
				
			System.out.println("host["+i+"]=" + hosts[i].getName());
			Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 "+hosts[i].getName());
		    int returnVal = p1.waitFor();
		        //System.out.println(returnVal);
		    boolean reachable = (returnVal==0);
		    if (reachable) {
		    	System.out.println("The host " + hosts[i].getName()+" is reachable");
		    	liveHosts.add(hosts[i].getName());
		        alive = alive+1;
		    }
		    else {
		    	System.out.println("The host " + hosts[i].getName()+" is not reachable, check again");
		    	dead = dead+1;
		    }
			//System.out.println("Number of VMs in "+hosts[i].getName()+ ": "+VM.length);	
		}
		
		System.out.println("\n============ Ping Virtual Machines ============");
		
		for(int j=0; j<VM.length; j++) {	
			VirtualMachine vm = (VirtualMachine)VM[j];
			System.out.println("vm["+j+"]=" + VM[j].getName());
			System.out.println("vm's alarm state for "+VM[j].getName()+" = " + vm.getTriggeredAlarmState());
			
			Process p2 = java.lang.Runtime.getRuntime().exec("ping -c 1 "+vm.getGuest().getIpAddress());
	     	int returnValVM = p2.waitFor();
	     	boolean reachableVM = (returnValVM==0);
	     	if (reachableVM) {
	     		System.out.println("The VM " + VM[j].getName()+" is reachable");
	     	    aliveVM = aliveVM+1;
	     	}
	     	else {
	     		System.out.println("The VM " + VM[j].getName()+" is not reachable, check again");
	  	       	deadVM = deadVM+1;
	  	       	if (VM[j].getTriggeredAlarmState()==null) {
	  	       		System.out.println(VM[j].getName()+" is down. Hence, the VM is being migrated to "+liveHosts.get(0));
	  	       		MigrateVM mvm = new MigrateVM();
	  	       		mvm.MigrateVM (VM[j].getName(),liveHosts.get(0));
	  	       	}
	     	}
	     	System.out.println(" ");
		}
				
				//hi.getServerConnection().logout();
			//}
			//else {
				//System.out.println("Vhost "+hosts[i].getName()+" is down");	
				//System.out.println("\n");
			//}
		System.out.println("The total host in datacenter = " + hosts.length);
		System.out.println("The alive host in datacenter = " + alive);
		System.out.println("The alive hosts in datacenter = " + liveHosts);
		System.out.println("The alive VMs in datacenter = " + aliveVM);
		
	}

	public static void main(String[] args) throws Exception {
		PingVM pvm = new PingVM();
		pvm.checkStatus();
	}
}
