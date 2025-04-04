  import React from "react";
  import { Link } from "react-router-dom";

  const Walkthrough = () => {
    return (
      <div className="container">
        <h1>Walkthrough</h1>
                go to <a className="link" href="https://www.instagram.com/accounts/access_tool/" target="_blank" rel="noopener noreferrer">Instagram</a> and download your data
                <br />
                make sure you download it in JSON format
                <br />
        <Link to="/" className="link">Go back</Link>
      </div>
    );
  };

  export default Walkthrough;
