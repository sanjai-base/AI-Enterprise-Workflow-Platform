'use client';

import { useState, useRef, useEffect } from 'react';
import { FiUploadCloud, FiFileText, FiMoreVertical, FiSearch, FiCpu } from 'react-icons/fi';

export default function DocumentsPage() {
  const [isDragging, setIsDragging] = useState(false);
  const [docs, setDocs] = useState<any[]>([]);
  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    fetchDocuments();
  }, []);

  const fetchDocuments = async () => {
    try {
      const res = await fetch('http://localhost:8085/api/v1/documents');
      if (res.ok) {
        const data = await res.json();
        setDocs(data);
      }
    } catch (e) {
      console.error("Failed to fetch documents", e);
    }
  };

  const uploadFile = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
      const res = await fetch('http://localhost:8085/api/v1/documents/upload', {
        method: 'POST',
        body: formData
      });
      if (res.ok) {
        alert('File uploaded successfully!');
        fetchDocuments();
      } else {
        alert('Upload failed.');
      }
    } catch (e) {
      alert('Error connecting to document-service');
    }
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      uploadFile(e.target.files[0]);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      uploadFile(e.dataTransfer.files[0]);
    }
  };

  const summarizeDocument = async (docName: string) => {
    try {
      // Show loading indicator
      alert(`Connecting to AI Copilot to analyze ${docName}...`);
      
      const res = await fetch('http://localhost:8084/api/v1/ai/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ message: `Please provide a 3-bullet point summary of the document named: ${docName}` })
      });

      if (res.ok) {
        const data = await res.text();
        alert(`🤖 AI Summary for ${docName}:\n\n${data}`);
      } else {
        alert("AI Service returned an error.");
      }
    } catch (e) {
      alert("Failed to connect to the AI Service.");
    }
  };

  return (
    <div className="flex flex-col h-full space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Document Center</h1>
          <p className="text-muted-foreground mt-1">Upload and manage your enterprise assets</p>
        </div>
        <div className="relative">
          <FiSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground" />
          <input 
            type="text" 
            placeholder="Search documents..." 
            className="pl-10 pr-4 py-2 bg-secondary/50 border-none rounded-md focus:outline-none focus:ring-1 focus:ring-primary w-64"
          />
        </div>
      </div>

      {/* Hidden File Input */}
      <input type="file" ref={fileInputRef} className="hidden" onChange={handleFileSelect} />

      {/* Drag & Drop Zone */}
      <div 
        onClick={() => fileInputRef.current?.click()}
        onDragOver={(e) => { e.preventDefault(); setIsDragging(true); }}
        onDragLeave={() => setIsDragging(false)}
        onDrop={handleDrop}
        className={`border-2 border-dashed rounded-xl p-12 text-center transition-colors cursor-pointer
          ${isDragging ? 'border-primary bg-primary/5' : 'border-border/50 hover:border-border hover:bg-secondary/20'}`}
      >
        <div className="flex justify-center mb-4">
          <div className="p-4 bg-secondary/50 rounded-full">
            <FiUploadCloud className="text-4xl text-primary" />
          </div>
        </div>
        <h3 className="text-lg font-semibold mb-1">Click or drag file to this area to upload</h3>
        <p className="text-sm text-muted-foreground">Support for a single or bulk upload. Maximum file size 50MB.</p>
      </div>

      {/* Document List */}
      <div className="bg-card rounded-xl border border-border/50 overflow-hidden shadow-sm flex-1">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="border-b border-border/50 bg-secondary/20 text-sm">
              <th className="py-4 px-6 font-medium text-muted-foreground">Document Name</th>
              <th className="py-4 px-6 font-medium text-muted-foreground">Size (Bytes)</th>
              <th className="py-4 px-6 font-medium text-muted-foreground">Uploaded</th>
              <th className="py-4 px-6 font-medium text-muted-foreground">Status</th>
              <th className="py-4 px-6 font-medium text-muted-foreground text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {docs.map((doc) => (
              <tr key={doc.id} className="border-b border-border/50 last:border-0 hover:bg-secondary/10 transition-colors">
                <td className="py-4 px-6">
                  <div className="flex items-center gap-3">
                    <div className="p-2 bg-blue-500/10 rounded-md text-blue-500">
                      <FiFileText size={18} />
                    </div>
                    <span className="font-medium">{doc.name}</span>
                  </div>
                </td>
                <td className="py-4 px-6 text-muted-foreground">{doc.size}</td>
                <td className="py-4 px-6 text-muted-foreground">{new Date(doc.uploadedAt).toLocaleString()}</td>
                <td className="py-4 px-6">
                  <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-500/10 text-green-500 border border-green-500/20">
                    {doc.status}
                  </span>
                </td>
                <td className="py-4 px-6 text-right">
                  <div className="flex justify-end gap-2">
                    <button 
                      onClick={() => summarizeDocument(doc.name)}
                      className="flex items-center gap-1 text-xs px-3 py-1.5 bg-primary/10 text-primary hover:bg-primary/20 rounded-md transition-colors font-medium"
                    >
                      <FiCpu /> AI Summary
                    </button>
                    <button className="p-1.5 text-muted-foreground hover:text-foreground rounded-md hover:bg-secondary transition-colors">
                      <FiMoreVertical />
                    </button>
                  </div>
                </td>
              </tr>
            ))}
            {docs.length === 0 && (
              <tr>
                <td colSpan={5} className="py-8 text-center text-muted-foreground">
                  No documents uploaded yet.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
