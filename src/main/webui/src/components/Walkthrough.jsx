  import React from "react";
  import { Link } from "react-router-dom";

  const Walkthrough = () => {
    return (
      <div className="container">
        <h1>Walkthrough</h1>
        Go to <a className="link" href="https://accountscenter.instagram.com/info_and_permissions/dyi/" target="_blank" rel="noopener noreferrer">Instagram</a> to download youe data.
        <ol>
          <li>Go to your account's data download page.</li>
          <li>Look for an option that says <strong>"Download your information"</strong> and click on it.</li>
          <li>Make sure to <strong>select JSON format</strong> for the download.</li>
          <li>
            Under the available categories, go to 
            <strong>"Some of your information"</strong> → 
            <strong>"Connections"</strong> → 
            <strong>"Followers and following"</strong>.
          </li>
          <li>When prompted for a date range, choose <strong>"All time"</strong> to ensure you get the complete list.</li>
          <li>Submit your request.</li>
        </ol>
       <p><strong>⚠️ Note:</strong> It may take <em>a day or two</em> for the download to be ready, depending on the amount of data you've selected.</p>
        <Link to="/" className="link">Go back</Link>
      </div>
    );
  };

  export default Walkthrough;
