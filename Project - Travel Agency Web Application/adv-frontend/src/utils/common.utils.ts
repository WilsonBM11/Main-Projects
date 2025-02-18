export function isNill(value: any) {
	return value === null || value === undefined;
}

export function formatDateDDMMYYYY(dateString: string | number | Date) {
	// Parse the date string using a reliable parsing method
	const date = new Date(dateString);
  
	// Check if parsing was successful (avoid potential errors)
	if (isNaN(date.getTime())) {
	  throw new Error('Invalid date string format');
	}
  
	const dd = date.getDate().toString().padStart(2, '0');
	const mm = (date.getMonth() + 1).toString().padStart(2, '0');
	const yyyy = date.getFullYear();
	return `${dd}/${mm}/${yyyy}`;
  }
