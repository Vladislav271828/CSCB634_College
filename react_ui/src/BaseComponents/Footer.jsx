function Footer() {
    const year = new Date();
    return (
        <footer className="app-footer">
            <p>Powered by <a href="https://github.com/Vladislav271828/CSCB634_College">MVI Collegium</a> © {year.getFullYear()}</p>
        </footer>
    )
}

export default Footer