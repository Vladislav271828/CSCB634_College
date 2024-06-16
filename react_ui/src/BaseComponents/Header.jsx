import { Link } from "react-router-dom";
import logo from "../Images/logo.png"

function Header() {
    return (
        <Link to={"/"}>
            <header className='app-header prevent-select'>
                <img src={logo}></img>
                <h2>University Name</h2>
            </header>
        </Link>
    )
}

export default Header