import FileUpload from "./components/FileUpload";
import { Link } from "react-router-dom";
import './App.css';

function Home() {
  return (
    <div className="container">
    <h1> Faux friend finder  </h1>
    <p> Upload your <Link to="/walkthrough" className="link"> IG data zip</Link> to find out who is not following you back </p>
      <FileUpload />
    </div>
  );
}

export default Home;
