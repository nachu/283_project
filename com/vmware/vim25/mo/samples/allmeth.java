package com.vmware.vim25.mo.samples;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Network;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.samples.vm.MigrateVM;

public class allmeth {
	static ScheduledExecutorService sexecs = Executors.newSingleThreadScheduledExecutor();
	public void HelloVM () throws MalformedURLException{
		
		long start = System.currentTimeMillis();
		URL url = new URL("https://130.65.132.219/sdk");
		ServiceInstance si = null;
		try {
			si = new ServiceInstance(url, "root", "12!@qwQW", true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("time taken:" + (end-start));
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity mes = null;
		try {
			mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", "Team09_Ubuntu_Nachu");
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		VirtualMachine vm = (VirtualMachine) mes; 	
		VirtualMachineConfigInfo vminfo = vm.getConfig();
		VirtualMachineCapability vmc = vm.getCapability();
		VirtualMachineSummary vms = vm.getSummary();
		VirtualMachineConfigSummary vmcs = vms.getConfig();
		VirtualMachineQuickStats vmqs = vms.getQuickStats();
		Network[] nw = null;
		try {
			nw = vm.getNetworks();
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			vm.getResourcePool();
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello " + vm.getName());
		System.out.println("GuestOS: " + vminfo.getGuestFullName());
		System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
		System.out.println("Summary: " + vm.getSummary());
		//System.out.println("Alarm state: " + vm.getAlarmActionEabled());
		System.out.println("Guest IP: " + vm.getGuest().getIpAddress());
		System.out.println("Guest is in host: " + vm.getParent());
		System.out.println("Quick Statistics: " + vms.getQuickStats());
		System.out.println("Runtime information: " + vms.getRuntime());
		System.out.println("CPU Allocation: " + vminfo.getCpuAllocation().getLimit());
		System.out.println("Number of CPU: " + vmcs.numCpu);
		System.out.println("CPU Hot enabled: " + vminfo.cpuHotAddEnabled);
		System.out.println("CPU Demand: " + vmqs.getOverallCpuDemand());
		System.out.println("CPU Usage: " + vmqs.getOverallCpuUsage());
		System.out.println("Network name : " + nw[0].getName());
		System.out.println("Network name : " + nw[0].toString());
		si.getServerConnection().logout();

		//static {
			
			sexecs.scheduleAtFixedRate(new Runnable() {
				public void run() {
			/*		SnapshotVM vmsp = new SnapshotVM();
					try {
						vmsp.refresh("130.65.132.214");
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
				}
			}, 0, 5, TimeUnit.SECONDS);	
	}
	
	public void PingVM() throws IOException, InterruptedException {
		//public void checkStatus() {
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
					
				System.out.println("host["+i+"]=" + hosts[i].getName());
				Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 "+hosts[i].getName());
			    int returnVal = p1.waitFor();
			       
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
			System.out.println("The total host in datacenter = " + hosts.length);
			System.out.println("The alive host in datacenter = " + alive);
			System.out.println("The alive hosts in datacenter = " + liveHosts);
			System.out.println("The alive VMs in datacenter = " + aliveVM);
			
		//}	
	}
}
