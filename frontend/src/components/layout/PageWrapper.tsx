import React from 'react';
import Sidebar from './Sidebar';
import Topbar from './Topbar';
import AICopilot from '../chat/AICopilot';

export default function PageWrapper({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-background relative">
      <Sidebar />
      <div className="md:ml-64 flex flex-col min-h-screen">
        <Topbar />
        <main className="flex-1 p-6 sm:p-10 relative">
          {children}
        </main>
      </div>
      <AICopilot />
    </div>
  );
}
