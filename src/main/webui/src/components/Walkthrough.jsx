  import React from "react";
  import { Link } from "react-router-dom";

  const Walkthrough = () => {
    return (
      <div className="container">
        <h1>Walkthrough</h1>
                go to <a className="link" href="https://accountscenter.instagram.com/info_and_permissions/dyi/" target="_blank" rel="noopener noreferrer">Instagram</a> and download your data
                <br />
                make sure you download it in JSON format. This process make take a day or two depending on what you chose to download.
                I recommend downloading "Some of your information" -> "Followers and following" under "Connections" and selecting from "All time".
                <br />
        <Link to="/" className="link">Go back</Link>
      </div>
    );
  };

  export default Walkthrough;
