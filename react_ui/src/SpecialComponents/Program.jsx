import React, { useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';

const Program = ({ title, backFunc, selectListValues }) => {

    const [fetchedData, setFetchedData] = useState(null);
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

    const generateYearsArray = (startYear) => {
        const years = [];
        for (let year = startYear; year >= 2023; year--) {
            years.push({
                id: year + 'S',
                label: `${year}/${year + 1} Spring`
            });
            years.push({
                id: year + 'A',
                label: `${year}/${year + 1} Autumn`
            });
        }
        return years;
    }

    const getOrdinal = (number) => {
        const ending = ["th", "st", "nd", "rd"]
        const temp = number % 100;
        return number + (ending[(temp - 20) % 10] || ending[temp] || ending[0]);
    }

    const handleBack = () => {
        if (backFunc) backFunc();
        else
            navigate("../dashboard", { relative: "path" });
    }

    const handleChange = async (e) => {
        setErrMsg("");
        setLoading(true)
        const year = e.target.value.match(/\d+/g)[0];
        let autumn;
        if (e.target.value.includes('A'))
            autumn = true;
        else
            autumn = false
        setSuccess(true)
        const url = formatString("/program/getByYearAndMajor/{0}/{1}", [year, selectListValues.majorId])
        try {
            const res = await axiosPrivate.get(url);

            const filteredRes = res.data.filter((data) => {
                return autumn ? data.autumn : !data.autumn
            })

            const urlTwo = formatString("/course/getAllByMajor/{0}", [selectListValues.majorId])
            const resTwo = await axiosPrivate.get(urlTwo);

            let placeholderData = [];
            resTwo.data.forEach((data) => {
                placeholderData[data.id] = data.signature + " " + data.name;
            })

            const prettyRes = filteredRes.reduce((acc, data) => {
                const id = data.courseId;
                if (!acc[data.educationYear]) {
                    acc[data.educationYear] = []
                }
                acc[data.educationYear].push({ ...data, courseId: placeholderData[id] })
                return acc
            }, [])

            setFetchedData(prettyRes);
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
                    <label>Semester</label>
                    <div>
                        <select
                            name="semester"
                            onChange={handleChange}
                            defaultValue={""}
                        >
                            <option value={""} disabled hidden></option>
                            {generateYearsArray(new Date().getFullYear()).map((year) => {
                                return <React.Fragment key={year.id}>
                                    <option
                                        value={year.id}>{year.label}</option>
                                </React.Fragment>
                            })}
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
                        fetchedData.map((data, index) => (
                            <table className="tg" key={index}>
                                <thead>
                                    <tr>
                                        <th className="tg-kiqf" colSpan="2">{getOrdinal(index)} School Year</th>
                                    </tr>
                                    <tr>
                                        <th className="tg-g6sd only-first">Course Name</th>
                                        <th className="tg-grsd only-first">Teachers</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {data.map((course) => (<tr>
                                        <td className="tg-0pky" key={course.id}>{course.courseId}</td>
                                        <td className="tg-dvpl">{course.professors.map((teacher) =>
                                            teacher.firstname + (teacher.lastname ? " " + teacher.lastname : "")).join(", ")}</td>
                                    </tr>))}
                                </tbody>
                            </table>
                        ))
                        : <p>No program available for selected semester.</p>
                    : <p>Loading...</p> : <></>}
        </div>

    )
}

export default Program
