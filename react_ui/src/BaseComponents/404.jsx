import { Link } from "react-router-dom"

function FourOhFour() {
    return (
        <div style={{ margin: "10vh auto" }}>
            <h1 style={{
                color: "#464646",
                fontSize: "min(32vw, 150px)",
                lineHeight: "1.2"
            }}>404</h1>
            <p style={{ margin: "0 10vh" }}>The page you are looking for is not available. <Link
                to="/" replace>
                <span>Click here</span>
            </Link> to go back to the home page.</p>
        </div >
    )
}

export default FourOhFour