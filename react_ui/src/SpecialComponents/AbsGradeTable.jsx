import React, { useContext, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import UserContext from '../Context/UserProvider';

const AbsGradeTable = ({ title, type }) => {

    const [fetchedData, setFetchedData] = useState(null);
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);
    const [loading, setLoading] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    const { id } = useContext(UserContext);

    function formatString(string, params) {
        const idReplacedString = string.replace("{id}", id)
        return idReplacedString.replace(/{(\d+)}/g, (match, index) => {
            return typeof params[index] !== 'undefined' ? params[index] : match;
        });
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
        try {
            const url = formatString("/enrollment/getAllByStudentAndYear/{id}/{0}", [year])
            const res = await axiosPrivate.get(url);

            const filteredRes = res.data.filter((data) => {
                return autumn ? data.autumn : !data.autumn
            })

            const urlTwo = formatString("/course/getAllByStudentAndYear/{id}/{0}", [year])
            const resTwo = await axiosPrivate.get(urlTwo);

            let placeholderData = [];
            resTwo.data.forEach((data) => {
                placeholderData[data.id] = data.signature + " " + data.name;
            })

            if (type == "GRADE") {
                //doing backflips in my code wasting an hour for an aesthetic choice nobody will even care about :^)
                const prettyResPromises = filteredRes.map(async (data) => {
                    const urlThree = formatString("/grade/getAllByEnrollment/{0}/{1}", [data.courseId, year])
                    const resThree = await axiosPrivate.get(urlThree);
                    const prettyGrades = resThree.data.map((gradeField) => {
                        const match = data.grades.find((item) => item.id.gradeId == gradeField.id)
                        if (match)
                            return { name: gradeField.name, score: match.score }
                        else return { name: gradeField.name, score: null }
                    })
                    const id = data.courseId;
                    return { ...data, courseId: placeholderData[id], grades: prettyGrades }
                })
                const prettyRes = await Promise.all(prettyResPromises)
                setFetchedData(prettyRes);
            }
            else {
                const prettyRes = filteredRes.map((data) => {
                    const id = data.courseId;
                    return { ...data, courseId: placeholderData[id] }
                })
                setFetchedData(prettyRes);
            }

            setLoading(false)
        } catch (err) {
            setLoading(false)
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    };

    function generateYearsArray(startYear) {
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

    return (
        <div className='component-container' style={{ paddingBottom: "1.5rem" }}>
            {title ? <h1>{title}</h1> : <></>}
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}

            <form className='form-structure-container' onSubmit={(e) => { e.preventDefault }}>
                <div className='table-input'>
                    {/* Assuming table will always be semester selection */}
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
                    <button type='button' onClick={() => navigate("../dashboard", { relative: "path" })}>
                        Go Back
                    </button>
                </div>
            </form>
            {fetchedData != null ?
                !loading ?
                    fetchedData?.length > 0 ?
                        type == "ABSENCE" ?
                            fetchedData.map((course) => (
                                <table className="tg" key={course.id}>
                                    <thead>
                                        <tr>
                                            <th className="tg-kiqf" colSpan="2">{course.courseId}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                            course.absences.map((absence) => (
                                                <tr key={absence.id}>
                                                    <td className="tg-0pky">{Intl.DateTimeFormat('en-US', { year: 'numeric', month: 'long', day: 'numeric' }).format(new Date(absence.date))}</td>
                                                    <td className="tg-dvpl">{absence?.note}</td>
                                                </tr>
                                            ))
                                        }
                                        <tr>
                                            <td className="tg-fymr">Total Absences:</td>
                                            <td className="tg-6ic8">{course.absences.length}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            ))
                            : type == "GRADE" ?
                                fetchedData.map((course) => (
                                    <table className="tg" key={course.id}>
                                        <thead>
                                            <tr>
                                                <th className="tg-kiqf" colSpan="2">{course.courseId}</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {
                                                course.grades.map((grade) => (
                                                    <tr key={grade.id}>
                                                        <td className="tg-0pky">{grade.name}</td>
                                                        <td className="tg-dvpl">{grade.score && (grade.score / 100).toFixed(2)}</td>
                                                    </tr>
                                                ))
                                            }
                                            <tr>
                                                <td className="tg-fymr">Final Grade:</td>
                                                <td className="tg-6ic8">{course.finalGrade && (course.finalGrade / 100).toFixed(2)}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                )) : <></>
                        : <p>You are not enrolled in any courses this semester.</p>
                    : <p>Loading...</p> : <></>}
        </div>

    )
}

export default AbsGradeTable
