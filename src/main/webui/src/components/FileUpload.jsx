import { useState } from "react";
import  "./FileUpload.css";


export default function FileUpload() {
  const [file, setFile] = useState(null);
  const [uploaded, setUploaded] = useState(false);
  const [names, setNames] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) return alert("Please select a file first.");

    setLoading(true);
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("/api/unzip", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("File upload failed");

      const data = await response.json();
      const uuid = data.uuid;

      const nonFollowerResponse = await fetch(`/api/connections/${uuid}/usersNotFollowingBack`);
      const nonFollowers = await nonFollowerResponse.json();
      const users = nonFollowers.users;
      setNames(users);
      setUploaded(true);
    } catch (error) {
      console.error("Error:", error);
      alert("Upload failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="p-4 max-w-md mx-auto border rounded-lg shadow-lg text-center">
          {!uploaded? (
            <div class="submit-form">
                <input type="file" accept=".zip" onChange={handleFileChange} className="mb-2" />
                <button
                  onClick={handleUpload}
                  className="bg-blue-500 text-white px-4 py-2 rounded"
                  disabled={loading}
                >
                  {loading ? "Uploading..." : "Upload"}
                </button>
            </div>
            ): (
                <div className="names-box">
                    {names.map((name, index) => (
                        <div key={index} className="name-item">{name}</div>
                    ))}
                </div>
                )}
            </div>
    </div>
  );
}
