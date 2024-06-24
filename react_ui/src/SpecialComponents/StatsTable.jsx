import React, { useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import { mean, mode, median, sampleVariance, sampleStandardDeviation, sampleSkewness, sum } from 'simple-statistics';

const StatsTable = ({ title, name, require, backFunc }) => {

    const [fetchedData, setFetchedData] = useState(null);
    const [isGrade, setIsGrade] = useState(false);
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);
    const [loading, setLoading] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    const formatString = (string, params) => {
        return string.replace(/{(\d+)}/g, (match, index) => {
            return typeof params[index] !== 'undefined' ? params[index] : match;
        });
    }

    const handleBack = () => {
        if (backFunc) backFunc();
        else
            navigate("../dashboard", { relative: "path" });
    }

    const handleChange = async (e) => {
        setLoading(true)
        setErrMsg("")
        setSuccess(true)
        setIsGrade(e.target.value == "GRADE")
        const preUrl = e.target.value == "GRADE" ? "/enrollment/getGradesBy/{0}/{1}" : "/absence/getNumberOfAbsencesBy/{0}/{1}"
        const url = formatString(preUrl, require);
        try {
            const res = await axiosPrivate.get(url);
            console.log(res.data);
            if (e.target.value == "GRADE")
                setFetchedData(res.data.filter(x => x != null).map(x => x / 100));
            else setFetchedData(res.data)
            setLoading(false)
        } catch (err) {
            setSuccess(false)
            setLoading(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    return (
        <div className='component-container' style={{ paddingBottom: "1.5rem" }}>
            {title ? <h1>{title}</h1> : <></>}
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}

            <form className='form-structure-container' onSubmit={(e) => { e.preventDefault }}>
                <div className='table-input'>
                    <label>Type of Statistics</label>
                    <div>
                        <select
                            name="semester"
                            onChange={handleChange}
                            defaultValue={""}
                        >
                            <option value={""} disabled hidden></option>
                            <option value={"GRADE"}>Grades</option>
                            <option value={"ABSENCE"}>Absences</option>
                        </select>
                    </div>
                </div>
                <div className="btn table-back-btn">
                    <button type='button' onClick={() => handleBack()}>
                        Go Back
                    </button>
                </div>
            </form>
            {fetchedData != null ?
                !loading ?
                    fetchedData?.length > 0 ?
                        <>
                            <table className="tg">
                                <thead>
                                    <tr>
                                        <th className="tg-kiqf" colSpan="2">{`${name.toUpperCase()} ${require[0]} ${isGrade ? "Grade" : "Absence"} Statistics`}</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td className="tg-0pky">Mean</td>
                                        <td className="tg-dvpl">{isGrade ? mean(fetchedData).toFixed(2) : mean(fetchedData).toPrecision(2)}</td>
                                    </tr>
                                    <tr>
                                        <td className="tg-0pky">Mode</td>
                                        <td className="tg-dvpl">{isGrade ? mode(fetchedData).toFixed(2) : mode(fetchedData)}</td>
                                    </tr>
                                    <tr>
                                        <td className="tg-0pky">Median</td>
                                        <td className="tg-dvpl">{isGrade ? median(fetchedData).toFixed(2) : median(fetchedData)}</td>
                                    </tr>
                                    <tr>
                                        <td className="tg-0pky">Sample Variance</td>
                                        <td className="tg-dvpl">{fetchedData?.length >= 2 ? sampleVariance(fetchedData).toPrecision(5) : "Not enough data."}</td>
                                    </tr>
                                    <tr>
                                        <td className="tg-0pky">Sample Standard Deviation</td>
                                        <td className="tg-dvpl">{fetchedData?.length >= 2 ? sampleStandardDeviation(fetchedData).toPrecision(5) : "Not enough data."}</td>
                                    </tr>
                                    <tr>
                                        <td className="tg-0pky">Sample Skewness</td>
                                        <td className="tg-dvpl">{fetchedData?.length >= 3 ? sampleSkewness(fetchedData).toPrecision(5) : "Not enough data."}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </>
                        : <p>No data available for selection.</p>
                    : <p>Loading...</p> : <></>}
        </div>

    )
}

export default StatsTable
