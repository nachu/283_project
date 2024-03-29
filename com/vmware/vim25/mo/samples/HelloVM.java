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
import java.net.URL;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class HelloVM 
{
	//static ScheduledExecutorService sexecssnap = Executors.newSingleThreadScheduledExecutor();
	//static ScheduledExecutorService sexecsping = Executors.newSingleThreadScheduledExecutor();
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		URL url = new URL("https://130.65.132.211/sdk");
		ServiceInstance si = new ServiceInstance(url, "root", "12!@qwQW", true);
		long end = System.currentTimeMillis();
		System.out.println("time taken:" + (end-start));
		Folder rootFolder = si.getRootFolder();
		String name = rootFolder.getName();
		System.out.println("root:" + name);
		ManagedEntity mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", "Team09_Ubuntu_Nachu");
		//if(mes==null || mes.length ==0)
		//{
		//	return;
		//}
		VirtualMachine vm = (VirtualMachine) mes; 	
		VirtualMachineConfigInfo vminfo = vm.getConfig();
		VirtualMachineCapability vmc = vm.getCapability();
		VirtualMachineSummary vms = vm.getSummary();
		VirtualMachineConfigSummary vmcs = vms.getConfig();
		VirtualMachineQuickStats vmqs = vms.getQuickStats();
		Network [] nw = vm.getNetworks();
		//ManagedEntityStatus mes = vmqs.getGuestHeartbeatStatus();
		vm.getResourcePool();
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
			
		/*sexecssnap.scheduleAtFixedRate(new Runnable() {
			public void run() {
				SnapshotVM vmsp = new SnapshotVM();
				try {
					vmsp.refresh("130.65.132.214");
				}
				catch (InterruptedException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				//System.out.println("print nachu");
			}
		}, 0, 50, TimeUnit.SECONDS);
		
		/*sexecsping.scheduleAtFixedRate(new Runnable() {
			public void run() {
			/*	PingVM pvm = new PingVM();
					try {
						pvm.checkStatus();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
			}
		}, 0, 5000, TimeUnit.SECONDS);*/
	}

}
