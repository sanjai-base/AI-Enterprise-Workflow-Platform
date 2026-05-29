'use client';

import React, { useState } from 'react';

type Step = {
  id: string;
  type: 'MANUAL' | 'APPROVAL' | 'AI_TASK' | 'NOTIFICATION';
  name: string;
};

export default function WorkflowBuilder() {
  const [steps, setSteps] = useState<Step[]>([]);
  const [workflowName, setWorkflowName] = useState('Untitled Workflow');

  const addStep = (type: Step['type']) => {
    setSteps([...steps, { id: Date.now().toString(), type, name: `New ${type} Step` }]);
  };

  const removeStep = (id: string) => {
    setSteps(steps.filter(s => s.id !== id));
  };

  const [savedWorkflowId, setSavedWorkflowId] = useState<string | null>(null);
  const [savedInstanceId, setSavedInstanceId] = useState<string | null>(null);

  const saveWorkflow = async () => {
    try {
      const response = await fetch('http://localhost:8083/api/v1/workflows', {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'X-Tenant-Id': '123e4567-e89b-12d3-a456-426614174000', // Dummy UUID
          'X-User-Id': '123e4567-e89b-12d3-a456-426614174001'   // Dummy UUID
        },
        body: JSON.stringify({ 
          name: workflowName, 
          definitionJson: JSON.stringify(steps) 
        })
      });
      const data = await response.json();
      setSavedWorkflowId(data.id);
      alert(`Workflow Saved successfully to Database! ID: ${data.id}`);
    } catch (e) {
      alert("Error saving workflow");
    }
  };

  const executeWorkflow = async () => {
    if (!savedWorkflowId) return alert("Save workflow first!");
    try {
      const response = await fetch(`http://localhost:8083/api/v1/workflows/${savedWorkflowId}/execute`, {
        method: 'POST',
        headers: { 'X-User-Id': '123e4567-e89b-12d3-a456-426614174001' }
      });
      const data = await response.json();
      setSavedInstanceId(data.id);
      alert(`Workflow Executing! Instance Status: ${data.status} | Current Step: ${data.currentStep}`);
    } catch (e) {
      alert("Error executing workflow");
    }
  };

  const approveStep = async () => {
    if (!savedInstanceId) return alert("Execute workflow first!");
    try {
      const response = await fetch(`http://localhost:8083/api/v1/workflows/instances/${savedInstanceId}/approve`, {
        method: 'POST'
      });
      const data = await response.json();
      alert(`Task Approved! State Machine transitioned to: ${data.status}`);
    } catch (e) {
      alert("Error approving step");
    }
  };

  return (
    <div className="flex flex-col h-[calc(100vh-120px)] bg-background">
      <div className="flex justify-between items-center mb-6">
        <input 
          type="text" 
          value={workflowName}
          onChange={(e) => setWorkflowName(e.target.value)}
          className="text-2xl font-bold bg-transparent border-none focus:outline-none focus:ring-1 focus:ring-primary rounded-md px-2"
        />
        <div className="flex gap-2">
          <button onClick={saveWorkflow} className="bg-secondary text-secondary-foreground px-4 py-2 rounded-md font-medium hover:bg-secondary/90">
            1. Save
          </button>
          <button onClick={executeWorkflow} disabled={!savedWorkflowId} className="bg-primary text-primary-foreground px-4 py-2 rounded-md font-medium hover:bg-primary/90 disabled:opacity-50">
            2. ▶ Execute
          </button>
          <button onClick={approveStep} disabled={!savedInstanceId} className="bg-green-600 text-white px-4 py-2 rounded-md font-medium hover:bg-green-700 disabled:opacity-50">
            3. ✓ Approve Task
          </button>
        </div>
      </div>

      <div className="flex flex-1 gap-6">
        {/* Sidebar Tools */}
        <div className="w-64 bg-card border border-border rounded-xl p-4 flex flex-col gap-3">
          <h3 className="font-semibold text-sm mb-2 text-muted-foreground uppercase tracking-wider">Nodes</h3>
          <button onClick={() => addStep('MANUAL')} className="w-full bg-secondary hover:bg-secondary/80 text-secondary-foreground py-3 rounded-lg text-sm font-medium border border-border text-left px-4 flex items-center gap-2">
            <span className="w-2 h-2 bg-blue-500 rounded-full"></span> Manual Task
          </button>
          <button onClick={() => addStep('APPROVAL')} className="w-full bg-secondary hover:bg-secondary/80 text-secondary-foreground py-3 rounded-lg text-sm font-medium border border-border text-left px-4 flex items-center gap-2">
            <span className="w-2 h-2 bg-yellow-500 rounded-full"></span> Approval Gate
          </button>
          <button onClick={() => addStep('AI_TASK')} className="w-full bg-secondary hover:bg-secondary/80 text-secondary-foreground py-3 rounded-lg text-sm font-medium border border-border text-left px-4 flex items-center gap-2">
            <span className="w-2 h-2 bg-purple-500 rounded-full"></span> AI Processing
          </button>
          <button onClick={() => addStep('NOTIFICATION')} className="w-full bg-secondary hover:bg-secondary/80 text-secondary-foreground py-3 rounded-lg text-sm font-medium border border-border text-left px-4 flex items-center gap-2">
            <span className="w-2 h-2 bg-green-500 rounded-full"></span> Notification
          </button>
        </div>

        {/* Canvas */}
        <div className="flex-1 bg-accent/20 border border-border rounded-xl p-8 overflow-y-auto relative flex flex-col items-center">
          {steps.length === 0 ? (
            <div className="absolute inset-0 flex items-center justify-center text-muted-foreground">
              Click a node on the left to start building your workflow.
            </div>
          ) : (
            <div className="w-full max-w-md flex flex-col items-center gap-4">
              <div className="w-48 py-2 bg-card border-2 border-primary border-dashed rounded-full text-center text-sm font-bold text-primary">
                START
              </div>
              
              {steps.map((step, index) => (
                <React.Fragment key={step.id}>
                  <div className="w-0.5 h-8 bg-border"></div>
                  <div className="w-full bg-card border border-border shadow-sm rounded-xl p-4 flex justify-between items-center group">
                    <div>
                      <p className="font-semibold text-sm">{step.name}</p>
                      <p className="text-xs text-muted-foreground mt-1">{step.type}</p>
                    </div>
                    <button onClick={() => removeStep(step.id)} className="text-muted-foreground hover:text-destructive opacity-0 group-hover:opacity-100 transition-opacity">
                      <svg xmlns="http://www.w3.org/2001/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clipRule="evenodd" /></svg>
                    </button>
                  </div>
                </React.Fragment>
              ))}

              <div className="w-0.5 h-8 bg-border"></div>
              <div className="w-48 py-2 bg-card border-2 border-muted-foreground border-dashed rounded-full text-center text-sm font-bold text-muted-foreground">
                END
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
